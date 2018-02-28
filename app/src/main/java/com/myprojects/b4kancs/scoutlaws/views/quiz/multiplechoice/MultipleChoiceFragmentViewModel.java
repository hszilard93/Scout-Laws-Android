package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by hszilard on 26-Feb-18.
 */

public class MultipleChoiceFragmentViewModel extends ViewModel {
    private static final String LOG_TAG = MultipleChoiceFragment.class.getSimpleName();
    private static final int NUMBER_OF_QUESTIONS = 10;

    public final int turnLimit = 5;

    private final ArrayList<ScoutLaw> scoutLaws;

    private int turnCount = 1;
    private Random random = new Random();
    private ScoutLaw answer;
    private ArrayList<ScoutLaw> options = new ArrayList<>(4);
    private ArrayList<Integer> usedLaws = new ArrayList<>(10);
    private ArrayList<Integer> usedOptions = new ArrayList<>(4);

    public MultipleChoiceFragmentViewModel() {
        scoutLaws = Repository.getInstance().getLaws();
        newTurn();
    }

    private void newTurn() {
        int answerIndex;
        do {
            answerIndex = random.nextInt(NUMBER_OF_QUESTIONS);
        } while (usedLaws.contains(answerIndex));
        usedLaws.add(answerIndex);
        answer = scoutLaws.get(answerIndex);

        options.clear();
        usedOptions.clear();
        options.add(answer);
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
