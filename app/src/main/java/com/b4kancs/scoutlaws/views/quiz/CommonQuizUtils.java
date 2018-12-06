package com.b4kancs.scoutlaws.views.quiz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.b4kancs.scoutlaws.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;
import static com.b4kancs.scoutlaws.logger.Logger.log;

/**
 * Created by hszilard on 15-Apr-18.
 * Class that contains some useful functionality common to multiple quiz types.
 */
public final class CommonQuizUtils {
    private static final String LOG_TAG = CommonQuizUtils.class.getSimpleName();

    private CommonQuizUtils() {
    }

    /* Make an animated* FragmentTransaction to replace the current fragment */
    public static FragmentTransaction getFragmentTransaction(@NonNull ViewGroup container,
                                                             FragmentManager manager,
                                                             Fragment targetFragment) {
        log(DEBUG, LOG_TAG, "getFragmentTransaction(..)");
        FragmentTransaction transaction = manager.beginTransaction();
        if (areAnimationsEnabled(container.getContext()))
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(container.getId(), targetFragment);
        return transaction;
    }

    /* Initialize and show the DialogFragment that displays the result */
    public static void showResultDialogFragment(ViewGroup container,
                                                Activity activity,
                                                FragmentManager fragmentManager,
                                                Fragment retryFragment,
                                                AbstractSharedViewModel sharedViewModel) {
        log(INFO, LOG_TAG, "showResultDialogFragment(..)");

        Bundle args = new Bundle();
        args.putInt("score", sharedViewModel.getScore());
        args.putInt("totalScore", sharedViewModel.getTotalScore());
        args.putInt("totalPossibleScore", sharedViewModel.getTotalPossibleScore());
        args.putLong("timeSpent", sharedViewModel.timeSpent); // measured time in milliseconds
        args.putLong("bestTime", sharedViewModel.getBestTime());

        ResultDialogFragment resultDialog = new ResultDialogFragment();
        resultDialog.setArguments(args);
        resultDialog.setCancelable(false);
        /* What happens when the retry button is clicked */
        resultDialog.setOnRetryClicked(event -> {
            log(INFO, LOG_TAG, "ResultDialog Retry clicked.");
            resultDialog.dismiss();
            FragmentTransaction transaction = getFragmentTransaction(container, fragmentManager, retryFragment);
            transaction.commit();
            sharedViewModel.start();
        });
        resultDialog.setOnBackClicked(event -> {
            log(DEBUG, LOG_TAG, "ResultDialog Back clicked.");
            resultDialog.dismiss();
            activity.onBackPressed();
        });
        resultDialog.show(fragmentManager, "finishDialog");
    }

    public static void showCorrectFeedback(Context context, LayoutInflater inflater) {
        log(INFO, LOG_TAG, "showCorrectFeedback(..); Showing positive toast.");
        Toast toast = new Toast(context);
        View toastView = inflater.inflate(R.layout.toast_correct, null);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showIncorrectFeedback(Context context, LayoutInflater inflater) {
        log(INFO, LOG_TAG, "showIncorrectFeedback(..); Showing negative toast.");
        Toast toast = new Toast(context);
        View toastView = inflater.inflate(R.layout.toast_incorrect, null);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
