package com.b4kancs.scoutlaws.views.quiz.picker;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.FragmentPickBinding;
import com.b4kancs.scoutlaws.databinding.TextViewPickerFillBinding;
import com.b4kancs.scoutlaws.databinding.TextViewPickerWordBinding;
import com.nex3z.flowlayout.FlowLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.getFragmentTransaction;
import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.showCorrectFeedback;
import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.showIncorrectFeedback;
import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.showResultDialogFragment;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.vibrate;
import static com.b4kancs.scoutlaws.logger.Logger.log;

/**
 * Created by hszilard on 3-March-18.
 * This fragment presents a picker type quiz where words in the question need to be filled in by dragging
 * the correct options to their places.
 */
public class PickerFragment extends Fragment {
    public static final String FRAGMENT_TAG = "PICKER_FRAGMENT";
    private static final String LOG_TAG = PickerFragment.class.getSimpleName();

    private PickerSharedViewModel sharedViewModel;
    private PickerViewModel viewModel;
    private FragmentPickBinding binding;
    private ViewGroup container;
    private FlowLayout questionFlow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log(DEBUG, LOG_TAG, "onCreateView(..)");
        this.container = container;

        sharedViewModel = ViewModelProviders.of(getActivity()).get(PickerSharedViewModel.class);
        PickerViewModelFactory viewModelFactory = new PickerViewModelFactory(sharedViewModel);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PickerViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pick, container, false);

        binding.setSharedViewModel(sharedViewModel);
        binding.setViewModel(viewModel);
        questionFlow = binding.included.flowQuestion;

        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        log(DEBUG, LOG_TAG, "setUpViews()");
        setUpQuestionFlow();

        binding.included.buttonHelp.setOnClickListener(helpButtonOnClickListener);
        binding.included.buttonCheck.setOnClickListener(checkButtonOnClickListener);
        binding.included.buttonClear.setOnClickListener(clearButtonOnClickListener);
        binding.included.buttonGiveUp.setOnClickListener(giveUpButtonListener);
        binding.included.buttonNext.setOnClickListener(nextButtonOnClickListener);
        binding.included.buttonFinish.setOnClickListener(finishButtonOnClickListener);
        // This line makes the layout animate not only visibility or view addition/removal changes
        // but other changes to its children too. We need it for width-change animations specifically
        if (areAnimationsEnabled(getContext())) {
            LayoutTransition transition = binding.included.flowQuestion.getLayoutTransition();
            if (transition != null)
                transition.enableTransitionType(LayoutTransition.CHANGING);
        }
    }

    /* I tried moving the entire questionFlow-setup into a @BindingAdapter, but experienced a big performance hit */
    private void setUpQuestionFlow() {
        log(DEBUG, LOG_TAG, "setUpQuestionFlow()");
        for (int i = 0; i < viewModel.getQuestionItems().size(); i++) {
            String item = viewModel.getQuestionItems().get(i);
            /* Check if it should be a word or a placeholder */
            if (item != null) {
                TextView wordView = makeQuestionWordView(item, questionFlow);
                questionFlow.addView(wordView);
            } else {
                if (viewModel.userAnswers.get(i) == null)
                    /* Register the index of the placeholder view with the ViewModel. They will observe the ViewModel. */
                    viewModel.addUserAnswer(i, null);

                TextViewPickerFillBinding fillBinding = DataBindingUtil.inflate(getLayoutInflater(),
                        R.layout.text_view_picker_fill, container, false);
                fillBinding.setObservableText(viewModel.userAnswers.get(i));
                fillBinding.getRoot().setOnDragListener(onOptionDrag);
                questionFlow.addView(fillBinding.getRoot());
            }
        }
    }

    private View.OnDragListener onOptionDrag = (view, event) -> {
        TextView subject = (TextView) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                log(INFO, LOG_TAG, "DragEvent.ACTION_DROP");
                int i = questionFlow.indexOfChild(view);
                viewModel.addUserAnswer(i, viewModel.optionDragSuccess());
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                log(INFO, LOG_TAG, "DragEvent.ACTION_DROP_ENDED");
                /* If the drag failed, restore the dragged view */
                if (!event.getResult()) {
                    viewModel.optionDragRestore();
                    log(INFO, LOG_TAG, "Drag failed.");
                }
                break;
        }
        return true;
    };

    private View.OnClickListener helpButtonOnClickListener = view -> {
        log(INFO, LOG_TAG, "Help button clicked.");
        viewModel.help();
        vibrate(getContext(), 300);
    };

    private View.OnClickListener giveUpButtonListener = view -> {
        log(INFO, LOG_TAG, "Give up button clicked.");
        viewModel.giveUp();
    };

    private View.OnClickListener checkButtonOnClickListener = view -> {
        log(INFO, LOG_TAG, "Check button clicked.");
        boolean result = viewModel.evaluateUserAnswers();

        if (result) {
            showCorrectFeedback(getContext(), getLayoutInflater());
        } else {
            showIncorrectFeedback(getContext(), getLayoutInflater());
            vibrate(getContext(), 300);
        }
    };

    private View.OnClickListener clearButtonOnClickListener = view -> {
        log(INFO, LOG_TAG, "Clear button clicked.");
        viewModel.clear();
    };

    private View.OnClickListener nextButtonOnClickListener = view -> {
        log(INFO, LOG_TAG, "Next button clicked.");
        transitionToNextQuestion();
    };

    private View.OnClickListener finishButtonOnClickListener = view -> {
        log(INFO, LOG_TAG, "Finish button clicked.");
        // Show the results
        showResultDialogFragment(container, getActivity(), getFragmentManager(), new PickerFragment(), sharedViewModel);
    };

    private TextView makeQuestionWordView(String word, ViewGroup parent) {
        TextViewPickerWordBinding wordBinding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.text_view_picker_word, parent, false);
        wordBinding.setNumber(viewModel.getScoutLaw().getLaw().getNumber());
        wordBinding.textWord.setText(word);
        return wordBinding.textWord;
    }

    private void transitionToNextQuestion() {
        log(DEBUG, LOG_TAG, "transitionToNextQuestion()");
        binding.unbind();
        FragmentTransaction transaction = getFragmentTransaction(container, getFragmentManager(), new PickerFragment());
        transaction.commit();
    }
}
