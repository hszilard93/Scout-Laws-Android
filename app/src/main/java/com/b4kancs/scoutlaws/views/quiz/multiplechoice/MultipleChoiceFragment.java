package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.FragmentMultipleBinding;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
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
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.vibrate;
import static com.b4kancs.scoutlaws.logger.Logger.log;

/**
 * Created by hszilard on 26-Feb-18.
 * This fragment presents a single multiple choice question and its possible answers.
 */
public class MultipleChoiceFragment extends Fragment {
    public static final String FRAGMENT_TAG = "MULTIPLE_FRAGMENT";
    private static final String LOG_TAG = MultipleChoiceFragment.class.getSimpleName();

    private MultipleChoiceSharedViewModel sharedViewModel;
    private MultipleChoiceViewModel viewModel;
    private FragmentMultipleBinding binding;
    private ViewGroup container;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log(DEBUG, LOG_TAG, "onCreateView(..)");
        super.onCreateView(inflater, container, savedInstanceState);

        this.container = container;
        sharedViewModel = ViewModelProviders.of(getActivity()).get(MultipleChoiceSharedViewModel.class);
        MultipleChoiceViewModelFactory factory = new MultipleChoiceViewModelFactory(sharedViewModel);
        viewModel = ViewModelProviders.of(this, factory).get(MultipleChoiceViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple, container, false);
        binding.setViewModel(viewModel);
        binding.setSharedViewModel(sharedViewModel);

        setUpViews();

        return binding.getRoot();
    }

    public void finish() {
        sharedViewModel.finish();
    }

    private void setUpViews() {
        log(DEBUG, LOG_TAG, "setUpViews()");
        OptionsListAdapter listAdapter = new OptionsListAdapter(viewModel, getContext(), onOptionSelected);
        binding.linearOptions.setAdapter(listAdapter);
        binding.linearOptions.setOnItemClickListener(listAdapter.defaultItemClickListener);
        binding.buttonNext.setOnClickListener(nextButtonClickedListener);
        binding.buttonFinish.setOnClickListener(finishButtonOnClickedListener);
        if (sharedViewModel.isLastTurn.get()) {
            binding.buttonNext.setVisibility(View.GONE);
            binding.buttonFinish.setVisibility(View.VISIBLE);
        }
    }

    /* What happens when an answer is selected */
    private OptionsListAdapter.OptionSelectedCallback onOptionSelected = (adapter, view, scoutLaw) -> {
        log(DEBUG, LOG_TAG, "onOptionSelected(..)");
        if (viewModel.evaluateAnswer(scoutLaw)) {
            log(INFO, LOG_TAG, "Answer is correct.");
            showCorrectFeedback(getContext(), getLayoutInflater());
            endTurn(adapter);
        } else {
            log(INFO, LOG_TAG, "Answer is correct.");
            showIncorrectFeedback(getContext(), getLayoutInflater());
            view.setVisibility(View.GONE);
            vibrate(getContext(), 300);
            if (viewModel.getObservableState().get() == MultipleChoiceViewModel.State.DONE) {
                endTurn(adapter);
            }
        }
    };

    private void endTurn(OptionsListAdapter adapter) {
        log(DEBUG, LOG_TAG, "endTurn(..)");
        binding.linearOptions.setOnItemClickListener(adapter.disabledItemClickListener);

        if (sharedViewModel.isLastTurn.get()) {
            binding.buttonFinish.setEnabled(true);
        }
    }

    private void transitionToNextQuestion() {
        log(DEBUG, LOG_TAG, "transitionToNextQuestion()");
        binding.unbind();
        FragmentTransaction transaction = getFragmentTransaction(container, getFragmentManager(), new MultipleChoiceFragment());
        transaction.commit();
    }

    private View.OnClickListener nextButtonClickedListener = (button) -> {
        log(INFO, LOG_TAG, "Next button clicked.");
        transitionToNextQuestion();
    };

    private View.OnClickListener finishButtonOnClickedListener = (button) -> {
        log(INFO, LOG_TAG, "Finish button clicked.");
        // Show the results
        showResultDialogFragment(container, getActivity(), getFragmentManager(), new MultipleChoiceFragment(), sharedViewModel);
    };

    /* Makes the next button available when the turn is over */
    @BindingAdapter("nextButton_turnOver")
    public static void setNextButtonTurnOver(@NonNull Button button, MultipleChoiceViewModel.State state) {
        Resources resources = button.getResources();
        if (state == MultipleChoiceViewModel.State.DONE) {
            button.setEnabled(true);
            button.setTextColor(resources.getColor(R.color.colorPrimary));
        } else {
            button.setEnabled(false);
            button.setTextColor(resources.getColor(R.color.disabled_grey));
        }
    }

    /* Set the text of a Multiple choice question (e.g. "Which is the 1st law of scouts?") */
    @BindingAdapter("multipleQuestionText_number")
    public static void setMultipleQuestionText(@NonNull TextView textView, int i) {
        Resources resources = textView.getResources();
        String questionText;
        Locale currentLocale = textView.getContext().getResources().getConfiguration().locale;
        if (currentLocale.getLanguage().equals("en")) {
            String ordinal = getEnOrdinalOfNumber(i);
            questionText = resources.getString(R.string.multiple_quiz_question, ordinal);
        } else {
            questionText = resources.getString(R.string.multiple_quiz_question, Integer.toString(i));
        }
        textView.setText(questionText);
    }

    private static String getEnOrdinalOfNumber(int i) {
        switch (i) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return i + "th";
        }
    }
}
