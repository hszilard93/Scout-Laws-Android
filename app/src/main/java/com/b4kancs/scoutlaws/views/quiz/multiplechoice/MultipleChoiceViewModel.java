package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.logger.Logger.log;

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
        observableState.addOnPropertyChangedCallback(stateCallback);
        startTurn();
    }

    private void startTurn() {
        log(INFO, LOG_TAG, "startTurn(); New multiple choice turn started.");
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
        log(DEBUG, LOG_TAG, "evaluateAnswer(..)");
        if (scoutLaw == answer) {
            log(DEBUG, LOG_TAG, "The answer is correct.");
            observableState.set(State.DONE);
            if (tries == 0)
                shared.incScore();
            if (shared.isLastTurn.get())
                shared.finish();
            return true;
        } else {
            log(DEBUG, LOG_TAG, "The answer is incorrect.");
            tries += 1;
            if (tries == NUMBER_OF_OPTIONS - 1) {
                observableState.set(State.DONE);
                if (shared.isLastTurn.get())
                    shared.finish();
            }
            return false;
        }
    }

    private Observable.OnPropertyChangedCallback stateCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            log(INFO, LOG_TAG, "observableStateChangedTo: " + observableState.get());
        }
    };

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
