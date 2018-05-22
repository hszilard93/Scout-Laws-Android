package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.util.Log;

import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by hszilard on 26-Feb-18.
 * Question data.
 */
public class MultipleChoiceViewModel extends ViewModel {
    private static final String LOG_TAG = MultipleChoiceViewModel.class.getSimpleName();
    protected static final int NUMBER_OF_OPTIONS = 4;

    public enum State {IN_PROGRESS, DONE}

    private final ObservableField<State> observableState = new ObservableField<>(State.IN_PROGRESS);

    private final ArrayList<ScoutLaw> options = new ArrayList<>(NUMBER_OF_OPTIONS);
    private final MultipleChoiceSharedViewModel shared;
    private final List<ScoutLaw> scoutLaws;
    private ScoutLaw answer;
    private int tries = 0;

    MultipleChoiceViewModel(MultipleChoiceSharedViewModel shared) {
        this.shared = shared;
        scoutLaws = shared.getScoutLaws();
        startTurn();
    }

    private void startTurn() {
        Log.d(LOG_TAG, "New multiple choice turn started.");
        shared.incTurnCount();

        int answerIndex = shared.nextLawIndex();
        answer = scoutLaws.get(answerIndex);
        options.add(answer);

        Random random = new Random();
        for (int i = 1; i < 4; i++) {   // we already have 1 possible answer, the correct one
            int optionIndex;
            do {
                optionIndex = random.nextInt(shared.repository.getNumberOfScoutLaws());
            } while (options.contains(scoutLaws.get(optionIndex)));
            options.add(scoutLaws.get(optionIndex));
        }
        Collections.shuffle(options);
    }

    boolean evaluateAnswer(ScoutLaw scoutLaw) {
        if (scoutLaw == answer) {
            Log.d(LOG_TAG, "The answer is correct.");
            observableState.set(State.DONE);
            if (tries == 0)
                shared.incScore();
            if (shared.isLastTurn.get())
                shared.finish();
            return true;
        } else {
            Log.d(LOG_TAG, "The answer is incorrect.");
            if (++tries == NUMBER_OF_OPTIONS - 1) {
                observableState.set(State.DONE);
                if (shared.isLastTurn.get())
                    shared.finish();
            }
            return false;
        }
    }

    public ObservableField<State> getObservableState() {
        return observableState;
    }

    public ScoutLaw getAnswer() {
        return answer;
    }

    ArrayList<ScoutLaw> getOptions() {
        return options;
    }

}
