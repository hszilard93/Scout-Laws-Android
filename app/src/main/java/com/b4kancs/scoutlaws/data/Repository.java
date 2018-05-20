package com.b4kancs.scoutlaws.data;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by hszilard on 15-Feb-18.
 * The repository currently handles loading the scout laws from the resources.
 */
@Singleton
public class Repository {
    private static final String LOG_TAG = Repository.class.getSimpleName();
    private static final String TOTAL_SCORE_KEY = "TOTAL_SCORE";
    private static final String TOTAL_POSSIBLE_SCORE_KEY = "TOTAL_POSSIBLE_SCORE";

    private final ArrayList<ScoutLaw> laws = new ArrayList<>(10);
    private final ArrayList<PickAndChooseScoutLaw> pickAndChooseLaws = new ArrayList<>(10);
    /* Injected application resources and shared preferences */
    private final Resources resources;
    private final SharedPreferences preferences;

    private int numberOfScoutLaws;      // How many laws are there? This is not constant!
    private int totalScore;
    private int totalPossibleScore;

    @Inject
    public Repository(Resources resources, SharedPreferences preferences) {
        Log.d(LOG_TAG, "Constructing Repository instance.");
        this.resources = resources;
        this.preferences = preferences;
        loadLaws();
        loadUserData();
    }

    private void loadLaws() {
        Log.d(LOG_TAG, "Loading scout laws.");
        String packageName = "com.b4kancs.scoutlaws";
        // Check how many scout laws are there
        numberOfScoutLaws = resources.getInteger(resources.getIdentifier("number_of_laws", "integer", packageName));
        Log.d(LOG_TAG, "The number of scout laws is " + numberOfScoutLaws);
        /* Building the ScoutLaw and PickAndChooseScoutLaw objects by dynamically loading them from their resource files by constructing their names */
        for (int i = 0; i < numberOfScoutLaws; i++) {
            // ScoutLaw objects
            String text = resources.getString(
                    resources.getIdentifier("law_" + (i + 1), "string", packageName)
            );
            String desc = resources.getString(
                    resources.getIdentifier("law_" + (i + 1) + "_desc", "string", packageName)
            );
            String origDesc = resources.getString(
                    resources.getIdentifier("law_" + (i + 1) + "_desc_orig", "string", packageName)
            );

            ScoutLaw law = new ScoutLaw(i + 1, text, desc, origDesc);
            laws.add(law);
            // PickAndChooseScoutLaw objects
            String pickChooseText = resources.getString(
                    resources.getIdentifier("law_" + (i + 1) + "_pick", "string", packageName)
            );

            String[] optionsArray = resources.getStringArray(
                    resources.getIdentifier("law_" + (i + 1) + "_pick_options", "array", packageName)
            );
            ArrayList<String> pickChooseOptions = new ArrayList<>(Arrays.asList(optionsArray));

            PickAndChooseScoutLaw pickAndChooseScoutLaw = new PickAndChooseScoutLaw(law, pickChooseText, pickChooseOptions);
            pickAndChooseLaws.add(i, pickAndChooseScoutLaw);
        }
    }

    private void loadUserData() {
        totalScore = preferences.getInt(TOTAL_SCORE_KEY, 0);
        totalPossibleScore = preferences.getInt(TOTAL_POSSIBLE_SCORE_KEY, 0);
    }

    public void resetSharedPreferences() {
        preferences.edit().clear().apply();
    }

    public int getNumberOfScoutLaws() {
        return numberOfScoutLaws;
    }

    public ArrayList<ScoutLaw> getLaws() {
        return laws;
    }

    public ArrayList<PickAndChooseScoutLaw> getPickAndChooseLaws() {
        return pickAndChooseLaws;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalPossibleScore() {
        return totalPossibleScore;
    }

    public void increaseTotalScoreBy(int thisMuch) {
        totalScore += thisMuch;
        preferences.edit().putInt(TOTAL_SCORE_KEY, totalScore).apply();
    }

    public void increaseTotalPossibleScoreBy(int thisMuch) {
        totalPossibleScore += thisMuch;
        preferences.edit().putInt(TOTAL_POSSIBLE_SCORE_KEY, totalPossibleScore).apply();
    }

    public void resetScore() {
        totalScore = 0;
        totalPossibleScore = 0;
        preferences.edit()
                .putInt(TOTAL_SCORE_KEY, totalScore)
                .putInt(TOTAL_POSSIBLE_SCORE_KEY, totalPossibleScore)
                .apply();
    }
}
