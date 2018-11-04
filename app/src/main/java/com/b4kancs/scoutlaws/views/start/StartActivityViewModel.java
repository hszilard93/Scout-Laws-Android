package com.b4kancs.scoutlaws.views.start;

import android.arch.lifecycle.ViewModel;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.services.NotificationScheduler;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class StartActivityViewModel extends ViewModel {
    @Inject protected Repository repository;
    @Inject protected NotificationScheduler notificationScheduler;

    public StartActivityViewModel() {
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        notificationScheduler.schedule(false);
    }

    private ArrayList<ScoutLaw> scoutLaws;

    public ArrayList<ScoutLaw> scoutLaws() {
        if (scoutLaws == null)
            scoutLaws = repository.getScoutLaws();
        return scoutLaws;
    }
}
