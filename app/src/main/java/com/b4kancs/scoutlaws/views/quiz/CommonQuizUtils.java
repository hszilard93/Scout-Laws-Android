package com.b4kancs.scoutlaws.views.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.b4kancs.scoutlaws.R;

/**
 * Created by hszilard on 15-Apr-18.
 * Class that contains some useful functionality common to multiple quiz types.
 */
public final class CommonQuizUtils {
    private static final String LOG_TAG = CommonQuizUtils.class.getSimpleName();

    private CommonQuizUtils() {
    }

    /* Make an animated FragmentTransaction to replace the current fragment */
    public static FragmentTransaction getFragmentTransaction(@NonNull ViewGroup container,
                                                             FragmentManager manager,
                                                             Fragment targetFragment) {
        Log.d(LOG_TAG, "Building FragmentTransaction.");
        FragmentTransaction transaction = manager.beginTransaction();
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
        Log.d(LOG_TAG, "Attempting to show ResultDialogFragment.");

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
            Log.d(LOG_TAG, "ResultDialog retry callback executing.");
            resultDialog.dismiss();
            FragmentTransaction transaction = getFragmentTransaction(container, fragmentManager, retryFragment);
            transaction.commit();
            sharedViewModel.start();
        });
        resultDialog.setOnBackClicked(event -> {
            Log.d(LOG_TAG, "ResultDialog back pressed callback executing.");
            resultDialog.dismiss();
            activity.onBackPressed();
        });
        resultDialog.show(fragmentManager, "finishDialog");
    }
}
