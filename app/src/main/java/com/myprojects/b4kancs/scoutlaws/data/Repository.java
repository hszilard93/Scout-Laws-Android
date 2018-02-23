package com.myprojects.b4kancs.scoutlaws.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class Repository {
    private static final String LOG_TAG = Repository.class.getSimpleName();
    private static Repository instance;

    private final ArrayList<ScoutLaw> laws;
    private final Context context;

    private Repository(Context context) {
        this.context = context;
        laws = new ArrayList<>(10);
        initLaws();
    }

    public static Repository getInstance(Context context) {
        if (instance != null) {
            Log.d(LOG_TAG, "Old Repository instance returned.");
            return instance;
        }
        Log.d(LOG_TAG, "Making new Repository instance.");
        instance = new Repository(context);
        return instance;
    }

    private void initLaws() {
        Resources resources = context.getResources();
        String packageName = "com.myprojects.b4kancs.scoutlaws";
        for (int i = 0; i < 10; i++) {
            /* Loading the text resources by dynamically constructing their names */
            String text = resources.getString(resources
                    .getIdentifier("law_" + (i + 1), "string", packageName));
            String desc = resources.getString(resources
                    .getIdentifier("law_" + (i + 1) + "_desc", "string", packageName));

            ScoutLaw law = new ScoutLaw(i + 1, text, desc);
            laws.add(law);
        }
    }

    public ArrayList<ScoutLaw> getLaws() {
        return laws;
    }
}
