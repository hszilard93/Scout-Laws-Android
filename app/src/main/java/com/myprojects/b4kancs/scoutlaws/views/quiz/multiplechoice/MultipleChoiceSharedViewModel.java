package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by hszilard on 26-Feb-18.
 */

public class MultipleChoiceSharedViewModel extends ViewModel {
    private static final String LOG_TAG = MultipleChoiceFragment.class.getSimpleName();
    private final static int NUMBER_OF_QUESTIONS = 10;
    private final static int NUMBER_OF_OPTIONS = 4;

    /* We shouldn't ask the same question two times in a row even if the quiz is restarted */
    private static Integer lastAnswerIndex;
    /* These we need for data binding */
    public final ObservableBoolean correctGuessed = new ObservableBoolean();
    public final ObservableBoolean turnOver = new ObservableBoolean();
    public final int turnLimit = 5;

    private final ArrayList<ScoutLaw> scoutLaws;

    private int turnCount;
    private int tries;
    private Random random = new Random();
    private ScoutLaw answer;
    private ArrayList<ScoutLaw> options = new ArrayList<>(NUMBER_OF_OPTIONS);
    private ArrayList<Integer> usedLaws = new ArrayList<>(NUMBER_OF_QUESTIONS);
    private ArrayList<Integer> usedOptions = new ArrayList<>(NUMBER_OF_OPTIONS);

    public MultipleChoiceSharedViewModel() {
        scoutLaws = Repository.getInstance().getLaws();
    }

    public void reset() {
        lastAnswerIndex = -1;
        turnCount = 0;
        tries = 0;
        usedLaws.clear();
    }

    public void startNewTurn() {
        initNewTurn();

        int answerIndex;
        do {
            answerIndex = random.nextInt(NUMBER_OF_QUESTIONS);
        } while (usedLaws.contains(answerIndex) || answerIndex == lastAnswerIndex);
        usedLaws.add(answerIndex);
        lastAnswerIndex = answerIndex;
        answer = scoutLaws.get(answerIndex);

        options.clear();
        usedOptions.clear();
        options.add(answer);
        usedOptions.add(answerIndex);
        for (int i = 1; i < 4; i++) {
            int optionIndex;
            do {
                optionIndex = random.nextInt(NUMBER_OF_QUESTIONS);
            } while (usedOptions.contains(optionIndex));
            usedOptions.add(optionIndex);
            options.add(scoutLaws.get(optionIndex));
        }
        Collections.shuffle(options);
    }

    private void initNewTurn() {
        turnCount += 1;
        turnOver.set(false);
        correctGuessed.set(false);
        tries = 0;
    }

    public boolean isNewAnswerCorrect(ScoutLaw scoutLaw) {
        if (scoutLaw == answer) {
            correctGuessed.set(true);
            turnOver.set(true);
            return true;
        }
        else {
            if (++tries == NUMBER_OF_OPTIONS - 1)
                turnOver.set(true);
            return false;
        }
    }

    public int getTurnCount() {
        return turnCount;
    }

    public ScoutLaw getAnswer() {
        return answer;
    }

    public ArrayList<ScoutLaw> getOptions() {
        return options;
    }
}
