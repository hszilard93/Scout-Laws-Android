package com.b4kancs.scoutlaws.views.quiz;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.DialogResultMultipleBinding;
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModel;

/**
 * Created by hszilard on 02-Mar-18.
 * Show the results.
 */

public class ResultDialogFragment extends DialogFragment {
    private static final String LOG_TAG = ResultDialogFragment.class.getSimpleName();

    private DialogResultMultipleBinding binding;
    private View.OnClickListener onRetryClicked;    // since DialogFragment is not a 'real' fragment, these have to be method-injected
    private View.OnClickListener onBackClicked;     //
    private int score;                              //

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
        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        binding.setScore(score);

        if (onBackClicked != null)
            binding.backButton.setOnClickListener(onBackClicked);
        else
            binding.backButton.setEnabled(false);

        if (onRetryClicked != null)
            binding.retryButton.setOnClickListener(onRetryClicked);
        else
            binding.retryButton.setEnabled(false);
    }

    /* Please, no rotation, I beg thee!!! */
    @Override public void onResume() {
        super.onResume();
        // lock screen orientation
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    @Override public void onPause() {
        super.onPause();
        // set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    /* */

    public void setScore(int score) {
        this.score = score;
    }

    public void setOnRetryClicked(View.OnClickListener onRetryClickedListener) {
        this.onRetryClicked = onRetryClickedListener;
    }

    public void setOnBackClicked(View.OnClickListener onBackClicked) {
        this.onBackClicked = onBackClicked;
    }
}
