package com.b4kancs.scoutlaws.views.start;

import android.arch.lifecycle.ViewModel;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class StartActivityViewModel extends ViewModel {
    @Inject protected Repository repository;
    private ArrayList<ScoutLaw> scoutLaws;

    public ArrayList<ScoutLaw> scoutLaws() {
        if (scoutLaws == null) {
            ScoutLawApp.getInstance().getApplicationComponent().inject(this);
            scoutLaws = repository.getLaws();
        }
        return scoutLaws;
    }
}
