package com.b4kancs.scoutlaws.views.start;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.services.NotificationScheduler;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import static android.util.Log.INFO;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 15-Feb-18.
 */
public class StartActivityViewModel extends ViewModel {
    @Inject protected Repository repository;

    public StartActivityViewModel() {
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
    }

    private ArrayList<ScoutLaw> scoutLaws;

    public ArrayList<ScoutLaw> scoutLaws() {
        scoutLaws = repository.getScoutLaws();
        return scoutLaws;
    }
}