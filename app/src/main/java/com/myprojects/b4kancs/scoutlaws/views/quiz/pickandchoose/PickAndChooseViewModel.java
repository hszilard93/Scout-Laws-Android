package com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by hszilard on 08-Mar-18.
 */

public class PickAndChooseViewModel extends ViewModel {
    private static final String LOG_TAG = PickAndChooseViewModel.class.getSimpleName();

    public enum State {IN_PROGRESS, CHECKABLE, DONE}

    private final ObservableField<State> observableState = new ObservableField<>(State.IN_PROGRESS);
    private final ObservableBoolean helpUsedUp = new ObservableBoolean(false);

    private PickAndChooseSharedViewModel shared;
    private PickAndChooseScoutLaw scoutLaw;
    private ArrayList<String> questionItems;
    private ArrayList<String> options;
    private ArrayList<String> correctAnswers = new ArrayList<>();
    /* We use a TreeMap here to keep track of each answer's (corresponding view's) index and to keep them sorted in the correct order */
    private TreeMap<Integer, String> userAnswers = new TreeMap<>();
    private boolean firstTry = true;

    PickAndChooseViewModel(PickAndChooseSharedViewModel shared) {
        this.shared = shared;
    }

    void startTurn() {
        Log.d(LOG_TAG, "Starting turn.");

        shared.incTurnCount();
        scoutLaw = shared.getPickChooseScoutLaws().get(shared.nextLawIndex());
        options = new ArrayList<>(scoutLaw.pickChooseOptions);
        parseQuestion(scoutLaw.pickChooseText);
        options.addAll(correctAnswers);

        Collections.shuffle(options);
    }

    void parseQuestion(String questionText) {
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

    void addUserAnswer(String answer, int i) {
        Log.d(LOG_TAG, "Adding answer.");

        options.remove(answer);
        userAnswers.put(i, answer);
        if (userAnswers.size() >= correctAnswers.size()) {
            observableState.set(State.CHECKABLE);
        }
    }

    void clearUserAnswers() {
        Log.d(LOG_TAG, "Clearing answers.");

        options.addAll(userAnswers.values());
        userAnswers.clear();
        observableState.set(State.IN_PROGRESS);
    }

    boolean checkAnswers() {
        Log.d(LOG_TAG, "Checking answers.");

        if (userAnswers.size() != correctAnswers.size()) {
            // Something went wrong!
            Log.e(LOG_TAG, "Incorrect state: userAnswers size is " + userAnswers.size()
                    + " but correctAnswers size is " + correctAnswers.size());
            return false;
        }

        String[] answers = userAnswers.values().toArray(new String[0]);
        for (int i = 0; i < correctAnswers.size(); i++)
            if (!correctAnswers.get(i).equals(answers[i])) {
                firstTry = false;
                return false;
            }

        if (firstTry)
            shared.incCorrectAtFirst();
        observableState.set(State.DONE);
        return true;
    }

    void helpUsed() {
        firstTry = false;
    }

    void giveUp() {
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

    public ArrayList<String> getOptions() {
        return options;
    }

    public TreeMap<Integer, String> getUserAnswers() {
        return userAnswers;
    }
}
