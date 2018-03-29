package com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.FragmentPickBinding;
import com.myprojects.b4kancs.scoutlaws.databinding.TextViewPickChooseWordBinding;
import com.myprojects.b4kancs.scoutlaws.views.quiz.ResultDialogFragment;
import com.nex3z.flowlayout.FlowLayout;

import java.util.Random;

/**
 * Created by hszilard on 3-March-18.
 * This fragment presents a pick and choose type quiz where words in the question need to be filled in by dragging
 * the correct options to their places.
 */
public class PickAndChooseFragment extends Fragment implements View.OnLongClickListener, View.OnDragListener {
    private static final String LOG_TAG = PickAndChooseFragment.class.getSimpleName();

    private PickAndChooseSharedViewModel sharedViewModel;
    private PickAndChooseViewModel viewModel;
    private FragmentPickBinding binding;
    private ViewGroup container;
    private FlowLayout questionFlow;
    private FlowLayout optionsFlow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;

        sharedViewModel = ViewModelProviders.of(getActivity()).get(PickAndChooseSharedViewModel.class);
        PickAndChooseViewModelFactory viewModelFactory = new PickAndChooseViewModelFactory(sharedViewModel);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PickAndChooseViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pick, container, false);

        binding.setSharedViewModel(sharedViewModel);
        binding.setViewModel(viewModel);
        questionFlow = binding.included.questionFlowLayout;
        optionsFlow = binding.included.optionsFlowLayout;

        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        setUpQuestionFlow();
        setUpOptionsFlow();

        binding.included.helpButton.setOnClickListener(helpButtonOnClickListener);
        binding.included.checkButton.setOnClickListener(checkButtonOnClickListener);
        binding.included.clearButton.setOnClickListener(clearButtonOnClickListener);
        binding.included.giveUpButton.setOnClickListener(giveUpButtonListener);
        binding.included.forwardButton.setOnClickListener(forwardButtonOnClickListener);
        binding.included.finishButton.setOnClickListener(finishButtonOnClickListener);
    }

    private void setUpQuestionFlow() {
        questionFlow.removeAllViews();

        for (int i = 0; i < viewModel.getQuestionItems().size(); i++) {
            String item = viewModel.getQuestionItems().get(i);
            /* Check if it should be a word or a placeholder */
            if (item != null) {
                TextView wordView = makeQuestionWordView(item, questionFlow);
                questionFlow.addView(wordView);
            }
            else {
                /* Check if an answer has already been set for this item */
                String existingAnswer = viewModel.getUserAnswers().get(i);
                if (existingAnswer != null) {
                    TextView existingAnswerView = makeOptionView(existingAnswer, questionFlow);
                    existingAnswerView.setText(existingAnswer);
                    questionFlow.addView(existingAnswerView);
                }
                else {
                    TextView emptyView = makeEmptyView(questionFlow);
                    questionFlow.addView(emptyView);
                }
            }
        }
    }

    private void setUpOptionsFlow() {
        optionsFlow.removeAllViews();

        for (String item : viewModel.getOptions()) {
            TextView option = makeOptionView(item, optionsFlow);
            option.setOnLongClickListener(this);
            optionsFlow.addView(option);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        /* Check if view is eligible for drag & drop */
        if (view.getId() != R.id.option_textView)
            return false;

        ClipData clipData = ClipData.newPlainText(null, ((TextView)view).getText());
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(clipData, shadowBuilder, view, 0);
        view.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        boolean result = true;

        TextView subject = (TextView) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                Log.d(LOG_TAG, "DragEvent.ACTION_DROP");
                /* Replace empty view with option */
                TextView option = (TextView) getLayoutInflater().inflate(R.layout.text_view_pick_choose_option,
                        (ViewGroup)view.getParent(), false);
                option.setText(subject.getText());
                ViewGroup parent = (ViewGroup) view.getParent();
                int i = parent.indexOfChild(view);
                parent.removeView(view);
                parent.addView(option, i);
                viewModel.addUserAnswer(option.getText().toString(), i);
                /* */
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(LOG_TAG, "DragEvent.ACTION_DROP_ENDED");
                /* If the drag failed, restore the dragged view */
                if (!event.getResult())
                    subject.setVisibility(View.VISIBLE);
                break;
        }
        return result;
    }

    private void clear() {
        for (int key : viewModel.getUserAnswers().navigableKeySet()) {      // key == index in the ViewGroup
            String currentAnswer = ((TextView)questionFlow.getChildAt(key)).getText().toString();
            TextView optionView = makeOptionView(currentAnswer, optionsFlow);
            optionView.setOnLongClickListener(this);
            optionsFlow.addView(optionView);
        }
        viewModel.clearUserAnswers();
        setUpQuestionFlow();
    }

    private View.OnClickListener helpButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Help button clicked.");

        vibrate();

        int numberToEliminate = Math.min(viewModel.getOptions().size() / 3, 3);  // determines the max number of options to eliminate
        Random random = new Random();
        for (int i = 0; i < numberToEliminate; i++) {
            int index = random.nextInt(viewModel.getOptions().size());
            if (!viewModel.getCorrectAnswers().contains(viewModel.getOptions().get(index))) {
                viewModel.getOptions().remove(index);
                optionsFlow.removeViewAt(index);
            }
            else
                i--;
        }
        viewModel.helpUsed();
        if (viewModel.getOptions().size() <= viewModel.getCorrectAnswers().size() * 3)
            viewModel.getHelpUsedUp().set(true);     // you shouldn't use help too much
    };

    private View.OnClickListener giveUpButtonListener = view -> {
        Log.d(LOG_TAG, "Give up button clicked.");

        int answerI = 0;
        for (int i = 0; i < questionFlow.getChildCount(); i++) {
            TextView textView = (TextView) questionFlow.getChildAt(i);
            if (textView.getId() != R.id.question_word) {      // the view is either an empty view or an option view
                String answer = viewModel.getCorrectAnswers().get(answerI++);
                questionFlow.removeViewAt(i);
                questionFlow.addView(makeOptionView(answer, questionFlow), i);
                viewModel.getUserAnswers().put(i, answer);
            }
        }
        viewModel.giveUp();
    };

    private View.OnClickListener checkButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Check button clicked.");
        boolean result = viewModel.checkAnswers();
        if (result) {
            Toast toast = new Toast(getContext());
            View toastView = getLayoutInflater().inflate(R.layout.toast_correct, null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast toast = new Toast(getContext());
            View toastView = getLayoutInflater().inflate(R.layout.toast_incorrect, null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();

            vibrate();

            clear();
        }
    };

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    private View.OnClickListener clearButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Clear button clicked.");

        clear();
    };

    private View.OnClickListener forwardButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Forward button clicked.");
        transitionToNextQuestion();
    };

    private View.OnClickListener finishButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Forward button clicked.");
        transitionToFinishDialog();
    };

    private void transitionToNextQuestion() {
        binding.unbind();
        FragmentTransaction transaction = getMultipleChoiceFragmentTransaction(container, getFragmentManager());
        transaction.commit();
    }

    /* Show the results. */
    private void transitionToFinishDialog() {
        ResultDialogFragment resultDialog = new ResultDialogFragment();
        resultDialog.setCancelable(false);
        /* What happens when the retry button is clicked */
        resultDialog.setOnRetryClicked(event -> {
            Log.d(LOG_TAG, "ResultDialog Retry callback executing..");
            resultDialog.dismiss();
            FragmentTransaction transaction = getMultipleChoiceFragmentTransaction(container, getFragmentManager());
            transaction.commit();
        });
        resultDialog.setScore(sharedViewModel.getScore());
        resultDialog.show(getFragmentManager(), "finishDialog");
        sharedViewModel.reset();
    }

    /* Common transaction. Go to the next question. */
    public static FragmentTransaction getMultipleChoiceFragmentTransaction(@NonNull ViewGroup container,
                                                                           FragmentManager manager) {
        PickAndChooseFragment newFragment = new PickAndChooseFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(container.getId(), newFragment);
        return transaction;
    }

    private TextView makeQuestionWordView(String word, ViewGroup parent) {
        TextViewPickChooseWordBinding wordBinding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.text_view_pick_choose_word, parent, false);
        wordBinding.setNumber(viewModel.getScoutLaw().law.number);
        wordBinding.questionWord.setText(word);
        return wordBinding.questionWord;
    }

    private TextView makeEmptyView(ViewGroup parent) {
        TextView emptyView = (TextView) getLayoutInflater()
                .inflate(R.layout.text_view_pick_choose_empty, parent, false);
        emptyView.setOnDragListener(this);
        return emptyView;
    }

    private TextView makeOptionView(String text, ViewGroup parent) {
        TextView optionView = (TextView) getLayoutInflater().inflate(R.layout.text_view_pick_choose_option, parent, false);
        optionView.setText(text);
        return optionView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}