package com.myprojects.b4kancs.scoutlaws.data.model;

import java.util.ArrayList;

/**
 * Created by hszilard on 20-Mar-18.
 * Wrapper for ScoutLaw, has additional pick and choose quiz type specific data.
 */

public class PickAndChooseScoutLaw {
    public final ScoutLaw law;
    public final String pickChooseText;
    public final ArrayList<String> pickChooseOptions;

    public PickAndChooseScoutLaw(ScoutLaw law, String pickChooseText, ArrayList<String> pickChooseOptions) {
        this.law = law;
        this.pickChooseText = pickChooseText;
        this.pickChooseOptions = pickChooseOptions;
    }
}
