package com.myprojects.b4kancs.scoutlaws.views.start;

import android.arch.lifecycle.ViewModel;

import com.myprojects.b4kancs.scoutlaws.ScoutLawApp;
import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class StartActivityViewModel extends ViewModel {
    private ArrayList<ScoutLaw> scoutLaws;
    @Inject protected Repository repository;

    public ArrayList<ScoutLaw> scoutLaws() {
        if (scoutLaws == null) {
            ScoutLawApp.getInstance().getApplicationComponent().inject(this);
            scoutLaws = repository.getLaws();
        }
        return scoutLaws;
    }
}
