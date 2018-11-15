package com.b4kancs.scoutlaws.views.quiz;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.DialogResultBinding;
import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 02-Mar-18.
 * Show the results.
 */

public class ResultDialogFragment extends DialogFragment {
    private static final String LOG_TAG = ResultDialogFragment.class.getSimpleName();

    private DialogResultBinding binding;
    private Resources resources;
    private int score;
    private int totalScore;
    private int totalPossibleScore;
    private long timeSpent;
    private long bestTime;
    private View.OnClickListener onRetryClicked;    // These have to be injected in setter methods
    private View.OnClickListener onBackClicked;     //

    @Override
    public void onStart() {
        log(INFO, LOG_TAG, "onStart()");
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        log(DEBUG, LOG_TAG, "onCreateView(..)");
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_result, container, false);
        resources = getResources();

        Bundle args = getArguments();
        score = args.getInt("score");
        totalScore = args.getInt("totalScore");
        totalPossibleScore = args.getInt("totalPossibleScore");
        timeSpent = args.getLong("timeSpent");
        bestTime = args.getLong("bestTime");

        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        log(DEBUG, LOG_TAG, "setUpViews()");
        setUpStars();
        setUpCongratsText();
        setUpTotalScoreText();
        setUpTimeText();

        if (onBackClicked != null)
            binding.buttonBack.setOnClickListener(onBackClicked);
        else
            binding.buttonBack.setEnabled(false);

        if (onRetryClicked != null)
            binding.buttonRetry.setOnClickListener(onRetryClicked);
        else
            binding.buttonRetry.setEnabled(false);
    }

    public void setUpStars() {
        int emptyStarId = R.drawable.ic_star_border_48dp;
        int fullStarId = R.drawable.ic_star_full_48dp;

        binding.layoutStars.imageStar1.setImageResource(
                score >= 1 ? fullStarId : emptyStarId
        );
        binding.layoutStars.imageStar2.setImageResource(
                score >= 2 ? fullStarId : emptyStarId
        );
        binding.layoutStars.imageStar3.setImageResource(
                score >= 3 ? fullStarId : emptyStarId
        );
        binding.layoutStars.imageStar4.setImageResource(
                score >= 4 ? fullStarId : emptyStarId
        );
        binding.layoutStars.imageStar5.setImageResource(
                score >= 5 ? fullStarId : emptyStarId
        );
    }

    public void setUpCongratsText() {
        TextView congratsTextView = binding.textCongrats;
        if (score <= 1)
            congratsTextView.setText(resources.getString(R.string.congrats_low_score));
        else if (score <= 3)
            congratsTextView.setText(resources.getString(R.string.congrats_mid_score));
        else if (score <= 4)
            congratsTextView.setText(resources.getString(R.string.congrats_good_score));
        else
            congratsTextView.setText(resources.getString(R.string.congrats_perfect_score));
    }

    public void setUpTotalScoreText() {
        TextView totalScoreTextView = binding.textTotalScore;
        String text = resources.getString(R.string.total_score_text) + " " + totalScore + "/" + totalPossibleScore;
        totalScoreTextView.setText(text);
    }

    public void setUpTimeText() {
        TextView timeTextView = binding.textTime;
        TextView bestTimeTextView = binding.textBestTime;
        Date time = new Date(timeSpent);
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.S");
        String timeText;
        String bestTimeText;

        /* When new best time, bestTime will actually be EQUAL to timeSpent, since the new time has already been saved by
         * the shared ViewModel before being passed to this DialogFragment */
        if (timeSpent <= bestTime && score == 5) {
            timeText = resources.getString(R.string.new_best_time_text) + " " + dateFormat.format(time);
            bestTimeText = "";
            bestTimeTextView.setVisibility(View.GONE);
        } else {
            if (score == 5)
                timeTextView.setTextColor(resources.getColor(R.color.colorPrimary));
            else
                timeTextView.setTextColor(Color.RED);

            timeText = resources.getString(R.string.time_text) + " " + dateFormat.format(time);
            bestTimeText = resources.getString(R.string.best_time_text) + " " + dateFormat.format(new Date(bestTime));
        }
        timeTextView.setText(timeText);
        bestTimeTextView.setText(bestTimeText);
    }

    /* Please, no rotation, I beg thee!!! */
    @Override
    public void onResume() {
        super.onResume();
        // lock screen orientation
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    @Override
    public void onPause() {
        super.onPause();
        // set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    /* */

    public void setOnRetryClicked(View.OnClickListener onRetryClickedListener) {
        log(INFO, LOG_TAG, "Retry button clicked.");
        this.onRetryClicked = onRetryClickedListener;
    }

    public void setOnBackClicked(View.OnClickListener onBackClicked) {
        log(INFO, LOG_TAG, "Back button clicked.");
        this.onBackClicked = onBackClicked;
    }
}
