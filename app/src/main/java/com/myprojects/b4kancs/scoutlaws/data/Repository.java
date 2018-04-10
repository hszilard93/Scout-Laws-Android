package com.myprojects.b4kancs.scoutlaws.data;

import android.content.res.Resources;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by hszilard on 15-Feb-18.
 */
@Singleton
public class Repository {
    private static final String LOG_TAG = Repository.class.getSimpleName();

    private final ArrayList<ScoutLaw> laws = new ArrayList<>(10);
    private final ArrayList<PickAndChooseScoutLaw> pickAndChooseLaws = new ArrayList<>(10);
    /* Context is needed to access application resources */
    private final Resources resources;

    @Inject
    public Repository(Resources resources) {
        Log.d(LOG_TAG, "Constructing Repository instance.");
        this.resources = resources;
        loadLaws();
    }

    private void loadLaws() {
        Log.d(LOG_TAG, "Loading scoutlaws.");

        String packageName = "com.myprojects.b4kancs.scoutlaws";
        /* Building the ScoutLaw and PickAndChooseScoutLaw objects by dynamically loading them from their resource files by constructing their names */
        for (int i = 0; i < 10; i++) {
            // ScoutLaw objects
            String text = resources.getString(resources
                    .getIdentifier("law_" + (i + 1), "string", packageName));
            String desc = resources.getString(resources
                    .getIdentifier("law_" + (i + 1) + "_desc", "string", packageName));
            String origDesc = resources.getString(resources
                    .getIdentifier("law_" + (i + 1) + "_desc_orig", "string", packageName));

            ScoutLaw law = new ScoutLaw(i + 1, text, desc, origDesc);
            laws.add(law);
            // PickAndChooseScoutLaw objects
            String pickChooseText = resources.getString(resources
                    .getIdentifier("law_" + (i + 1) + "_pick", "string", packageName));

            String[] optionsArray = resources.getStringArray(resources
                    .getIdentifier("law_" + (i + 1) + "_pick_options", "array", packageName));
            ArrayList<String> pickChooseOptions = new ArrayList<>(Arrays.asList(optionsArray));

            PickAndChooseScoutLaw pickAndChooseScoutLaw = new PickAndChooseScoutLaw(law, pickChooseText, pickChooseOptions);
            pickAndChooseLaws.add(i, pickAndChooseScoutLaw);
        }
    }

    public ArrayList<ScoutLaw> getLaws() {
        return laws;
    }

    public ArrayList<PickAndChooseScoutLaw> getPickAndChooseLaws() {
        return pickAndChooseLaws;
    }
}
