package com.b4kancs.scoutlaws.views.quiz;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.os.SystemClock;
import android.util.Log;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

/**
 * Created by hszilard on 08-Mar-18.
 */

public abstract class AbstractSharedViewModel extends ViewModel {
    protected static final int TURN_LIMIT = 5;    // The turn limit must never be larger than the number of questions
    private static final String LOG_TAG = AbstractSharedViewModel.class.getSimpleName();
    // We shouldn't ask the same question two times in a row even if the quiz is restarted. Preserved across quiz types.
    protected static Integer lastUsedLawIndex = -1;
    /* These fields have to be public to be accessible for data binding. */
    public final ObservableBoolean isLastTurn = new ObservableBoolean(false);
    public final ObservableBoolean chronoStart = new ObservableBoolean(true);

    protected ObservableInt turnCount = new ObservableInt(0);
    private ObservableLong baseTime = new ObservableLong(0);
    protected long timeSpent;
    @Inject public Repository repository;
    protected final ArrayList<Integer> usedLaws; // Questions we have asked in this quiz
    protected int score = 0;
    private Random random = new Random();

    protected AbstractSharedViewModel() {
        Log.d(LOG_TAG, "Constructing.");
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        usedLaws = new ArrayList<>(repository.getNumberOfScoutLaws());
    }

    public void start() {
        Log.d(LOG_TAG, "Starting/Resetting.");
        isLastTurn.set(false);
        chronoStart.set(true);
        baseTime.set(SystemClock.elapsedRealtime());
        turnCount.set(0);
        timeSpent = 0;
        usedLaws.clear();
        score = 0;
    }

    /* This law will be the subject of the next question. */
    public int nextLawIndex() {
        Log.d(LOG_TAG, "Generating next law index.");
        int index;
        do {
            index = random.nextInt(repository.getNumberOfScoutLaws());
        } while (usedLaws.contains(index) || index == lastUsedLawIndex);
        usedLaws.add(index);
        lastUsedLawIndex = index;
        return index;
    }

    /* finish() should be called when the last answer is DONE (before the finish button is clicked, for more accurate timing) */
    public void finish() {
        repository.increaseTotalScoreBy(score);
        repository.increaseTotalPossibleScoreBy(5);
        timeSpent = SystemClock.elapsedRealtime() - baseTime.get();
        chronoStart.set(false);
        long bestTime = getBestTime();
        if ((timeSpent < bestTime || bestTime == 0) && score == TURN_LIMIT)
            saveNewBestTime();
    }

    public ObservableInt getTurnCount() {     // public because of data binding
        return turnCount;
    }

    public void incTurnCount() {
        Log.d(LOG_TAG, "Increasing turn count. Current turn: " + turnCount);

        if (turnCount.get() < TURN_LIMIT)
            turnCount.set(turnCount.get() + 1);

        if (turnCount.get() == TURN_LIMIT)
            isLastTurn.set(true);
    }

    public int getScore() {
        return score;
    }

    public void incScore() {
        Log.d(LOG_TAG, "Increasing score. Current score: " + score);
        score += 1;
    }

    public int getTurnLimit() {
        return TURN_LIMIT;
    }

    public boolean isThisTheLastTurn() {
        return isLastTurn.get();
    }

    public int getTotalScore() {
        return repository.getTotalScore();
    }

    public int getTotalPossibleScore() {
        return repository.getTotalPossibleScore();
    }

    public abstract long getBestTime();

    protected abstract void saveNewBestTime();

    public ObservableLong getBaseTime() {
        return baseTime;
    }
}
