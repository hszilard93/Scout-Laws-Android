package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.FragmentMultipleBinding;
import com.myprojects.b4kancs.scoutlaws.views.quiz.ResultDialogFragment;

/**
 * Created by hszilard on 26-Feb-18.
 * This fragment presents a single multiple choice question and its possible answers.
 */

public class MultipleChoiceFragment extends Fragment {
    private static final String LOG_TAG = MultipleChoiceFragment.class.getSimpleName();

    private MultipleChoiceSharedViewModel sharedViewModel;
    private MultipleChoiceViewModel viewModel;
    private FragmentMultipleBinding binding;
    private ViewGroup container;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private void setUpViews() {
        OptionsListAdapter listAdapter = new OptionsListAdapter(viewModel, getContext(), onOptionSelected);
        binding.optionsLinearLayout.setAdapter(listAdapter);
        binding.optionsLinearLayout.setOnItemClickListener(listAdapter.defaultItemClickListener);
        binding.nextButton.setOnClickListener(nextButtonClickedListener);
        binding.finishButton.setOnClickListener(finishButtonOnClickedListener);
        if (sharedViewModel.isLastTurn.get()) {
            binding.nextButton.setVisibility(View.GONE);
            binding.finishButton.setVisibility(View.VISIBLE);
        }
    }

    /* What happens when an answer is selected */
    private OptionsListAdapter.OptionSelectedCallback onOptionSelected = (adapter, view, scoutLaw) -> {
        if (viewModel.isAnswerCorrect(scoutLaw)) {
            Toast toast = new Toast(getContext());
            View toastView = getLayoutInflater().inflate(R.layout.toast_correct, null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            endTurn(adapter);
        } else {
            Toast toast = new Toast(getContext());
            View toastView = getLayoutInflater().inflate(R.layout.toast_incorrect, null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();

            view.setVisibility(View.GONE);
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(300);
            if (viewModel.getObservableState().get() == MultipleChoiceViewModel.State.DONE) {
                endTurn(adapter);
            }
        }
    };

    private void endTurn(OptionsListAdapter adapter) {
        binding.optionsLinearLayout.setOnItemClickListener(adapter.disabledItemClickListener);

        if (sharedViewModel.isLastTurn.get()) {
            binding.finishButton.setEnabled(true);
        }
    }

    private void transitionToNextQuestion() {
        binding.unbind();
        FragmentTransaction transaction = getMultipleChoiceFragmentTransaction(container, getFragmentManager());
        transaction.commit();
    }

    /* Common transaction. Go to the next question. */
    public static FragmentTransaction getMultipleChoiceFragmentTransaction(@NonNull ViewGroup container,
                                                                           FragmentManager manager) {
        MultipleChoiceFragment newFragment = new MultipleChoiceFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(container.getId(), newFragment);
        return transaction;
    }

    /* Show the results. */
    private void transitionToFinishDialog() {
        ResultDialogFragment resultDialog = new ResultDialogFragment();
        resultDialog.setCancelable(false);
        /* What happens when the retry button is clicked */
        resultDialog.setOnRetryClicked(event -> {
            Log.d(LOG_TAG, "ResultDialog retry callback executing..");
            sharedViewModel.reset();
            resultDialog.dismiss();
            FragmentTransaction transaction = getMultipleChoiceFragmentTransaction(container, getFragmentManager());
            transaction.commit();
        });
        resultDialog.setScore(sharedViewModel.getScore());
        resultDialog.show(getFragmentManager(), "finishDialog");
    }

    private View.OnClickListener nextButtonClickedListener = (button) -> {
        Log.d(LOG_TAG, "Next button clicked.");
        transitionToNextQuestion();
    };

    private View.OnClickListener finishButtonOnClickedListener = (button) -> {
        Log.d(LOG_TAG, "Finish button clicked.");
        transitionToFinishDialog();
    };

    /* Let's clean up some */
    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedViewModel = null;
        viewModel = null;
        binding = null;
        container = null;
        onOptionSelected = null;
        nextButtonClickedListener = null;
        finishButtonOnClickedListener = null;
    }

    /* Makes the next button available when the turn is over */
    @BindingAdapter({"nextButton_turnOver"})
    public static void setNextButtonTurnOver(@NonNull Button button, MultipleChoiceViewModel.State state) {
        Resources resources = button.getResources();
        if (state == MultipleChoiceViewModel.State.DONE) {
            button.setEnabled(true);
            button.setTextColor(resources.getColor(R.color.colorPrimary));
            button.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, resources.getDrawable(R.drawable.ic_keyboard_arrow_right_green_24dp), null);
        } else {
            button.setEnabled(false);
            button.setTextColor(resources.getColor(R.color.disabled_grey));
            button.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, resources.getDrawable(R.drawable.ic_keyboard_arrow_right_grey_24dp), null);
        }
    }
}
