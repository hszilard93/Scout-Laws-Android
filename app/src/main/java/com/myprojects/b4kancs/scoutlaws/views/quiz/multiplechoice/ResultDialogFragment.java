package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.DialogResultMultipleBinding;

/**
 * Created by hszilard on 02-Mar-18.
 * Show the results.
 */

public class ResultDialogFragment extends DialogFragment {
    private static final String LOG_TAG = ResultDialogFragment.class.getSimpleName();

    private DialogResultMultipleBinding binding;
    private MultipleChoiceSharedViewModel sharedViewModel;
    private View.OnClickListener onRetryClicked;    // since DialogFragment is not a 'real' fragment this has to be injected

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_result_multiple, container, false);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(MultipleChoiceSharedViewModel.class);
        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        binding.setScore(sharedViewModel.getScore());
        binding.okButton.setOnClickListener(okButtonClickedListener);
        if (onRetryClicked != null)
            binding.retryButton.setOnClickListener(onRetryClicked);
        else
            binding.retryButton.setEnabled(false);
    }

    private View.OnClickListener okButtonClickedListener = (button) -> {
        getFragmentManager().popBackStack();
        dismiss();
    };

    void setOnRetryClicked(View.OnClickListener onRetryClickedListener) {
        this.onRetryClicked = onRetryClickedListener;
    }
}
