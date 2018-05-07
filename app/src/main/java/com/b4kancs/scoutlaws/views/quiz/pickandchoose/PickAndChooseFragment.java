package com.b4kancs.scoutlaws.views.quiz.pickandchoose;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.FragmentPickBinding;
import com.b4kancs.scoutlaws.databinding.TextViewPickChooseFillBinding;
import com.b4kancs.scoutlaws.databinding.TextViewPickChooseWordBinding;
import com.b4kancs.scoutlaws.views.utils.CommonUtils;
import com.nex3z.flowlayout.FlowLayout;

import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.getFragmentTransaction;
import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.showResultDialogFragment;

/**
 * Created by hszilard on 3-March-18.
 * This fragment presents a pick and choose type quiz where words in the question need to be filled in by dragging
 * the correct options to their places.
 */
public class PickAndChooseFragment extends Fragment {
    public static final String FRAGMENT_TAG = "PICK_CHOOSE_FRAGMENT";
    private static final String LOG_TAG = PickAndChooseFragment.class.getSimpleName();

    private PickAndChooseSharedViewModel sharedViewModel;
    private PickAndChooseViewModel viewModel;
    private FragmentPickBinding binding;
    private ViewGroup container;
    private FlowLayout questionFlow;

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

        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        setUpQuestionFlow();

        binding.included.helpButton.setOnClickListener(helpButtonOnClickListener);
        binding.included.checkButton.setOnClickListener(checkButtonOnClickListener);
        binding.included.clearButton.setOnClickListener(clearButtonOnClickListener);
        binding.included.giveUpButton.setOnClickListener(giveUpButtonListener);
        binding.included.forwardButton.setOnClickListener(forwardButtonOnClickListener);
        binding.included.finishButton.setOnClickListener(finishButtonOnClickListener);
        // This line makes the layout animate not only visibility or view addition/removal changes
        // but other changes to its children too. We need it for width-change animations
        binding.included.questionFlowLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    }

    /* I tried moving the entire questionFlow-setup into a @BindingAdapter, but experienced big performance hit */
    private void setUpQuestionFlow() {
        for (int i = 0; i < viewModel.getQuestionItems().size(); i++) {
            String item = viewModel.getQuestionItems().get(i);
            /* Check if it should be a word or a placeholder */
            if (item != null) {
                TextView wordView = makeQuestionWordView(item, questionFlow);
                questionFlow.addView(wordView);
            } else {
                if (viewModel.getUserAnswers().get(i) == null)
                    /* Register the index of the placeholder view with the ViewModel. They will observe the ViewModel. */
                    viewModel.addUserAnswer(i, null);

                TextViewPickChooseFillBinding fillBinding = DataBindingUtil.inflate(getLayoutInflater(),
                        R.layout.text_view_pick_choose_fill, container, false);
                fillBinding.setObservableText(viewModel.getUserAnswers().get(i));
                fillBinding.getRoot().setOnDragListener(onOptionDrag);
                questionFlow.addView(fillBinding.getRoot());
            }
        }
    }

    private View.OnDragListener onOptionDrag = (view, event) -> {
        TextView subject = (TextView) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                Log.d(LOG_TAG, "DragEvent.ACTION_DROP");
                int i = questionFlow.indexOfChild(view);
                viewModel.addUserAnswer(i, subject.getText().toString());
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(LOG_TAG, "DragEvent.ACTION_DROP_ENDED");
                /* If the drag failed, restore the dragged view */
                if (!event.getResult())
                    viewModel.addOption(subject.getText().toString());
                break;
        }
        return true;
    };

    private View.OnClickListener helpButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Help button clicked.");
        viewModel.help();
        CommonUtils.vibrate(getContext(), 300);
    };

    private View.OnClickListener giveUpButtonListener = view -> {
        Log.d(LOG_TAG, "Give up button clicked.");
        viewModel.giveUp();
    };

    private View.OnClickListener checkButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Check button clicked.");
        boolean result = viewModel.check();

        if (result) {
            Toast toast = new Toast(getContext());
            View toastView = getLayoutInflater().inflate(R.layout.toast_correct, null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = new Toast(getContext());
            View toastView = getLayoutInflater().inflate(R.layout.toast_incorrect, null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();

            CommonUtils.vibrate(getContext(), 300);
        }
    };

    private View.OnClickListener clearButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Clear button clicked.");
        viewModel.clear();
    };

    private View.OnClickListener forwardButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Forward button clicked.");
        transitionToNextQuestion();
    };

    private View.OnClickListener finishButtonOnClickListener = view -> {
        Log.d(LOG_TAG, "Forward button clicked.");
        // Show the results
        showResultDialogFragment(container, getActivity(), getFragmentManager(), new PickAndChooseFragment(), sharedViewModel);
    };

    private TextView makeQuestionWordView(String word, ViewGroup parent) {
        TextViewPickChooseWordBinding wordBinding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.text_view_pick_choose_word, parent, false);
        wordBinding.setNumber(viewModel.getScoutLaw().getLaw().getNumber());
        wordBinding.wordTextView.setText(word);
        return wordBinding.wordTextView;
    }

    private void transitionToNextQuestion() {
        binding.unbind();
        FragmentTransaction transaction = getFragmentTransaction(container, getFragmentManager(), new PickAndChooseFragment());
        transaction.commit();
    }

    /* CommonUtils transaction. Go to the next question. */
    private static FragmentTransaction getMultipleChoiceFragmentTransaction(@NonNull ViewGroup container,
                                                                            FragmentManager manager) {
        PickAndChooseFragment newFragment = new PickAndChooseFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(container.getId(), newFragment);
        return transaction;
    }

}