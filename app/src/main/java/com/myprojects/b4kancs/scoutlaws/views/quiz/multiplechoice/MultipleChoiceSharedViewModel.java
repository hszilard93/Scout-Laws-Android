package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;

/**
 * Created by hszilard on 28-Feb-18.
 * This shared ViewModel contains quiz data relevant to more than one question.
 */

public class MultipleChoiceSharedViewModel extends ViewModel {
    static final int NUMBER_OF_QUESTIONS = 10;  // How many questions are there to choose from? This may be dynamic later.
    private static final String LOG_TAG = MultipleChoiceSharedViewModel.class.getSimpleName();
    private static final int TURN_LIMIT = 5;    // The turn limit must never be larger than the number of questions

    private final ArrayList<ScoutLaw> scoutLaws;    // This is constant
    ObservableBoolean isLastTurn = new ObservableBoolean(false);
    private ArrayList<Integer> usedLaws = new ArrayList<>(NUMBER_OF_QUESTIONS); // Questions we have asked in this quiz
    private Integer lastAnswerIndex = -1; // We shouldn't ask the same question two times in a row even if the quiz is restarted
    private int turnCount;
    private int score = 0;

    public MultipleChoiceSharedViewModel() {
        scoutLaws = Repository.getInstance().getLaws();
    }

    public void reset() {
        isLastTurn.set(false);
        turnCount = 0;
        score = 0;
        usedLaws.clear();
    }

    ArrayList<ScoutLaw> getScoutLaws() {
        return scoutLaws;
    }

    ArrayList<Integer> getUsedLaws() {
        return usedLaws;
    }

    public int getTurnCount() {     // public because of data binding
        return turnCount;
    }

    void incTurnCount() {
        turnCount += 1;
        if (turnCount >= TURN_LIMIT)
            isLastTurn.set(true);
    }

    int getScore() {
        return score;
    }

    void incCorrectAtFirst() {
        score += 1;
    }

    Integer getLastAnswerIndex() {
        return lastAnswerIndex;
    }

    void setLastAnswerIndex(Integer lastAnswerIndex) {
        this.lastAnswerIndex = lastAnswerIndex;
    }

    public int getTurnLimit() {
        return TURN_LIMIT;
    }
}
