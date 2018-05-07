package com.b4kancs.scoutlaws.views.quiz.pickandchoose;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by hszilard on 08-Mar-18.
 * This ViewModel contains all logic for a 'pick&choose quiz' turn.
 * The UI should reflect changes in the ViewModel, user input should trigger its actions.
 */
public class PickAndChooseViewModel extends ViewModel {
    private static final String LOG_TAG = PickAndChooseViewModel.class.getSimpleName();

    public enum State {IN_PROGRESS, CHECKABLE, DONE}

    private final ObservableField<State> observableState = new ObservableField<>(State.IN_PROGRESS);
    private final ObservableBoolean helpUsedUp = new ObservableBoolean(false);
    /* We use a TreeMap here to keep track of each answer's (corresponding view's) index and to keep them sorted in the correct order */
    private final TreeMap<Integer, ObservableField<String>> userAnswers = new TreeMap<>();
    private final ObservableArrayList<String> options = new ObservableArrayList<>();

    private final PickAndChooseSharedViewModel shared;
    private PickAndChooseScoutLaw scoutLaw;
    private ArrayList<String> questionItems;
    private final ArrayList<String> correctAnswers = new ArrayList<>();
    private boolean firstTry = true;

    PickAndChooseViewModel(PickAndChooseSharedViewModel shared) {
        this.shared = shared;
        startTurn();
    }

    private void startTurn() {
        Log.d(LOG_TAG, "Starting turn.");

        shared.incTurnCount();
        scoutLaw = shared.getPickChooseScoutLaws().get(shared.nextLawIndex());
        options.addAll(scoutLaw.getPickChooseOptions());
        parseQuestion(scoutLaw.getPickChooseText());
        options.addAll(correctAnswers);

        Collections.shuffle(options);
    }

    /* Process the question with format "Something something #interesting something". */
    private void parseQuestion(String questionText) {
        Log.d(LOG_TAG, "Parsing question.");

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
        Log.d(LOG_TAG, "Adding answer.");

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
        if (!options.contains(item))
            options.add(item);
    }

    /* Eliminate some options */
    void help() {
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

        observableState.set(State.IN_PROGRESS);
    }

    /* Clear the item from userAnswers and add it back to options */
    void clear() {
        Log.d(LOG_TAG, "Clearing answers.");

        for (ObservableField<String> item : userAnswers.values())
            if (item.get() != null) {
                options.add(item.get());
                item.set(null);
            }
        observableState.set(State.IN_PROGRESS);
    }

    boolean check() {
        Log.d(LOG_TAG, "Checking answers.");

        boolean correct = true;

        if (userAnswers.size() != correctAnswers.size()) {
            // Something went wrong!
            Log.e(LOG_TAG, "Incorrect state: userAnswers size is " + userAnswers.size()
                    + " but correctAnswers size is " + correctAnswers.size());
            correct = false;
        }

        Iterator<ObservableField<String>> userAnswersIterator = userAnswers.values().iterator();
        for (String correctAnswer : correctAnswers) {
            ObservableField<String> userAnswer = userAnswersIterator.next();
            if (!correctAnswer.equals(userAnswer.get())) {
                correct = false;
                options.add(userAnswer.get());
                userAnswer.set(null);
            }
        }

        if (!correct)
            firstTry = false;

        if (firstTry)
            shared.incCorrectAtFirst();

        observableState.set(correct ? State.DONE : State.IN_PROGRESS);
        return correct;
    }

    void giveUp() {
        Iterator<ObservableField<String>> userAnswersIterator = userAnswers.values().iterator();
        for (String correctAnswer : correctAnswers)
            userAnswersIterator.next().set(correctAnswer);

        observableState.set(State.DONE);
    }

    public ObservableField<State> getObservableState() {
        return observableState;
    }

    public ObservableBoolean getHelpUsedUp() {
        return helpUsedUp;
    }

    public PickAndChooseScoutLaw getScoutLaw() {
        return scoutLaw;
    }

    public ArrayList<String> getQuestionItems() {
        return questionItems;
    }

    public ArrayList<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public ObservableArrayList<String> getOptions() {
        return options;
    }

    public TreeMap<Integer, ObservableField<String>> getUserAnswers() {
        return userAnswers;
    }
}
