package com.myprojects.b4kancs.scoutlaws.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.ScoutLawApp;
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
//    private static Repository instance;

    private final ArrayList<ScoutLaw> laws;
    private final ArrayList<PickAndChooseScoutLaw> pickAndChooseLaws;
    /* Context is needed to access application resources */
    private final Context context;

    @Inject
    public Repository(Context context) {
        this.context = context;
        laws = new ArrayList<>(10);
        pickAndChooseLaws = new ArrayList<>(10);
        loadLaws();
    }

//    public static Repository getInstance() {
//        if (instance != null) {
//            Log.d(LOG_TAG, "Returning existing Repository instance.");
//            return instance;
//        }
//        Log.d(LOG_TAG, "Making new Repository instance.");
//        instance = new Repository();
//        return instance;
//    }

    private void loadLaws() {
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        Resources resources = context.getResources();
        String packageName = "com.myprojects.b4kancs.scoutlaws";
        for (int i = 0; i < 10; i++) {
            /* Loading the text resources by dynamically constructing their names */
            String text = resources.getString(resources
                    .getIdentifier("law_" + (i + 1), "string", packageName));
            String desc = resources.getString(resources
                    .getIdentifier("law_" + (i + 1) + "_desc", "string", packageName));
            String origDesc = resources.getString(resources
                    .getIdentifier("law_" + (i + 1) + "_desc_orig", "string", packageName));

            ScoutLaw law = new ScoutLaw(i + 1, text, desc, origDesc);
            laws.add(law);

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
