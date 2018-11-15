package com.b4kancs.scoutlaws.views.quiz.picker;

import com.b4kancs.scoutlaws.data.model.PickerScoutLaw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 08-Mar-18.
 * This ViewModel contains all logic for a picker quiz turn.
 * The UI should reflect changes in the ViewModel, user input should trigger its actions.
 */
public class PickerViewModel extends ViewModel {
    private static final String LOG_TAG = PickerViewModel.class.getSimpleName();

    public enum State {IN_PROGRESS, CHECKABLE, DONE}

    // These fields must be public for databound layouts to access them
    public final ObservableField<State> observableState = new ObservableField<>(State.IN_PROGRESS);
    public final ObservableBoolean helpUsedUp = new ObservableBoolean(false);
    public final ObservableArrayList<String> options = new ObservableArrayList<>();
    /* We use a TreeMap here to keep track of each answer's (corresponding view's) index and to keep them sorted in the correct order */
    public final TreeMap<Integer, ObservableField<String>> userAnswers = new TreeMap<>();

    protected final ArrayList<String> correctAnswers = new ArrayList<>();
    private final PickerSharedViewModel shared;

    private PickerScoutLaw scoutLaw;
    private ArrayList<String> questionItems;
    private boolean firstTry = true;

    PickerViewModel(PickerSharedViewModel shared) {
        this.shared = shared;
        startTurn();
    }

    private void startTurn() {
        log(INFO, LOG_TAG, "startTurn(); New picker turn started.");

        shared.incTurnCount();
        scoutLaw = shared.getPickerScoutLaws().get(shared.nextLawIndex());
        options.addAll(scoutLaw.getPickerOptions());
        parseQuestion(scoutLaw.getPickerText());
        options.addAll(correctAnswers);

        observableState.addOnPropertyChangedCallback(stateCallback);

        Collections.shuffle(options);
    }

    /* Process the question with format "Something something #interesting something". */
    private void parseQuestion(String questionText) {
        log(DEBUG, LOG_TAG, "parseQuestion(questionText = " + questionText + ")");

        questionItems = new ArrayList<>(Arrays.asList(questionText.split(" ")));
        for (int i = 0; i < questionItems.size(); i++) {
            String item = questionItems.get(i);
            if (item.charAt(0) == '#') {
                item = item.replaceAll("[# .,;?!]", "");
                correctAnswers.add(item);
                questionItems.set(i, null);
            }
        }
    }

    void addUserAnswer(int i, String newAnswer) {
        log(DEBUG, LOG_TAG, "addUserAnswer(i = " + i + ", newAnswer = " + newAnswer + ")");

        options.remove(newAnswer);
        if (userAnswers.get(i) == null)
            userAnswers.put(i, new ObservableField<>(newAnswer));
        else {
            String oldAnswer = userAnswers.get(i).get();
            if (oldAnswer != null)
                options.add(oldAnswer);
            userAnswers.get(i).set(newAnswer);
        }

        // If there are enough valid values in userAnswers, change state
        if (userAnswers.size() == correctAnswers.size()) {
            for (ObservableField<String> item : userAnswers.values())
                if (item.get() == null) return;
            observableState.set(State.CHECKABLE);
        }
    }

    void addOption(String item) {
        log(DEBUG, LOG_TAG, "addOption(item = " + item + ")");
        if (!options.contains(item))
            options.add(item);
    }

    /* Eliminate some options */
    void help() {
        log(DEBUG, LOG_TAG, "help()");
        int numberToEliminate = Math.min(options.size() / 3, 3);

        Random random = new Random();
        for (int i = 0; i < numberToEliminate; i++) {
            int index = random.nextInt(options.size());
            if (!correctAnswers.contains(options.get(index)))
                options.remove(index);
            else
                i--;
        }
        firstTry = false;
        if (options.size() <= correctAnswers.size() * 3)
            helpUsedUp.set(true);     // you shouldn't use help too much
    }

    /* Clear the item from userAnswers and add it back to options */
    void clear() {
        log(DEBUG, LOG_TAG, "clear()");

        for (ObservableField<String> item : userAnswers.values())
            if (item.get() != null) {
                options.add(item.get());
                item.set(null);
            }
        observableState.set(State.IN_PROGRESS);
    }

    /* If the answers are all correct, change state. Else remove wrong answers and add them back to options. */
    boolean evaluateUserAnswers() {
        log(DEBUG, LOG_TAG, "evaluateUserAnswers()");

        boolean correct = true;

        if (userAnswers.size() != correctAnswers.size()) {
            // Something went wrong!
            log(ERROR, LOG_TAG, "Incorrect state: userAnswers size is " + userAnswers.size()
                    + " but correctAnswers size is " + correctAnswers.size());
            correct = false;
        } else {
            Iterator<ObservableField<String>> userAnswersIterator = userAnswers.values().iterator();
            for (String correctAnswer : correctAnswers) {
                ObservableField<String> userAnswer = userAnswersIterator.next();
                if (!correctAnswer.equals(userAnswer.get())) {
                    correct = false;
                    options.add(userAnswer.get());
                    userAnswer.set(null);
                }
            }
        }

        if (!correct) {
            log(DEBUG, LOG_TAG, "Answer was incorrect.");
            firstTry = false;
        }

        if (firstTry)
            shared.incScore();

        if (correct) {
            log(DEBUG, LOG_TAG, "Answer was correct.");
            observableState.set(State.DONE);
            if (shared.isThisTheLastTurn())
                shared.finish();
        } else
            observableState.set(State.IN_PROGRESS);

        return correct;
    }

    /* Fill in the correct answers */
    void giveUp() {
        log(DEBUG, LOG_TAG, "giveUp()");
        Iterator<ObservableField<String>> userAnswersIterator = userAnswers.values().iterator();
        for (String correctAnswer : correctAnswers)
            userAnswersIterator.next().set(correctAnswer);

        observableState.set(State.DONE);
    }

    private Observable.OnPropertyChangedCallback stateCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            log(INFO, LOG_TAG, "observableStateChangedTo: " + observableState.get());
        }
    };

    public PickerScoutLaw getScoutLaw() {
        return scoutLaw;
    }

    public ArrayList<String> getQuestionItems() {
        return questionItems;
    }
}
