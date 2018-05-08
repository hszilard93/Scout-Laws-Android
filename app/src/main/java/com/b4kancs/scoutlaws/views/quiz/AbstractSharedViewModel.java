package com.b4kancs.scoutlaws.views.quiz;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

/**
 * Created by hszilard on 08-Mar-18.
 */

public abstract class AbstractSharedViewModel extends ViewModel {
    protected static final int TURN_LIMIT = 5;    // The turn limit must never be larger than the number of questions
    private static final String LOG_TAG = AbstractSharedViewModel.class.getSimpleName();

    public final ObservableBoolean isLastTurn = new ObservableBoolean(false);
    @Inject public Repository repository;
    protected final ArrayList<Integer> usedLaws; // Questions we have asked in this quiz
    protected Integer lastAnswerIndex = -1; // We shouldn't ask the same question two times in a row even if the quiz is restarted
    protected int turnCount;
    protected int score = 0;
    private Random random = new Random();

    protected AbstractSharedViewModel() {
        Log.d(LOG_TAG, "Constructing.");
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        usedLaws = new ArrayList<>(repository.getNumberOfScoutLaws());
    }

    public void reset() {
        Log.d(LOG_TAG, "Resetting.");
        isLastTurn.set(false);
        turnCount = 0;
        score = 0;
        usedLaws.clear();
    }

    /* This law will be the subject of the next question. */
    public int nextLawIndex() {
        Log.d(LOG_TAG, "Generating next law index.");
        int index;
        do {
            index = random.nextInt(repository.getNumberOfScoutLaws());
        } while (usedLaws.contains(index) || index == lastAnswerIndex);
        usedLaws.add(index);
        lastAnswerIndex = index;
        return index;
    }

    public int getTurnCount() {     // public because of data binding
        return turnCount;
    }

    public void incTurnCount() {
        Log.d(LOG_TAG, "Increasing turn count. Current turn: " + turnCount);
        if (turnCount < TURN_LIMIT)
            turnCount++;

        if (turnCount == TURN_LIMIT)
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
}
