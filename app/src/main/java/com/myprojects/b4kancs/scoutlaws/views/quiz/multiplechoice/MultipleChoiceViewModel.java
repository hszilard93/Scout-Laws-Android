package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModel.*;

/**
 * Created by hszilard on 26-Feb-18.
 * Question data.
 */

public class MultipleChoiceViewModel extends ViewModel {
    private static final String LOG_TAG = MultipleChoiceViewModel.class.getSimpleName();
    private final static int NUMBER_OF_OPTIONS = 4;

    /* These we need for data binding */
    public final ObservableBoolean correctGuessed = new ObservableBoolean(false);
    public final ObservableBoolean isTurnOver = new ObservableBoolean(false);

    private final MultipleChoiceSharedViewModel shared;
    private final ArrayList<ScoutLaw> scoutLaws;
    private int tries = 0;
    private Random random = new Random();
    private ScoutLaw answer;
    private ArrayList<ScoutLaw> options = new ArrayList<>(NUMBER_OF_OPTIONS);
    private ArrayList<Integer> usedOptions = new ArrayList<>(NUMBER_OF_OPTIONS);

    MultipleChoiceViewModel(MultipleChoiceSharedViewModel shared) {
        this.shared = shared;
        scoutLaws = shared.getScoutLaws();
    }

    void startNewTurn() {
        Log.d(LOG_TAG, "New multiple choice turn started.");
        shared.incTurnCount();

        int answerIndex;
        do {
            answerIndex = random.nextInt(NUMBER_OF_QUESTIONS);
        } while (shared.getUsedLaws().contains(answerIndex) || answerIndex == shared.getLastAnswerIndex());
        shared.getUsedLaws().add(answerIndex);
        shared.setLastAnswerIndex(answerIndex);
        answer = scoutLaws.get(answerIndex);

        options.add(answer);
        usedOptions.add(answerIndex);
        for (int i = 1; i < 4; i++) {   // we already have 1 possible answer, the correct one
            int optionIndex;
            do {
                optionIndex = random.nextInt(NUMBER_OF_QUESTIONS);
            } while (usedOptions.contains(optionIndex));
            usedOptions.add(optionIndex);
            options.add(scoutLaws.get(optionIndex));
        }
        Collections.shuffle(options);
    }

    boolean isNewAnswerCorrect(ScoutLaw scoutLaw) {
        if (scoutLaw == answer) {
            Log.d(LOG_TAG, "The answer is correct.");
            correctGuessed.set(true);
            isTurnOver.set(true);
            if (tries == 0)
                shared.incCorrectAtFirst();
            return true;
        }
        else {
            Log.d(LOG_TAG, "The answer is incorrect.");
            if (++tries == NUMBER_OF_OPTIONS - 1)
                isTurnOver.set(true);
            return false;
        }
    }

    public ScoutLaw getAnswer() {
        return answer;
    }

    ArrayList<ScoutLaw> getOptions() {
        return options;
    }

}
