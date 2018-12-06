package com.b4kancs.scoutlaws.views.start;

import com.b4kancs.scoutlaws.App;
import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

import static com.b4kancs.scoutlaws.logger.Logger.log;

/**
 * Created by hszilard on 15-Feb-18.
 */
public class StartActivityViewModel extends ViewModel {
    @Inject protected Repository repository;

    public StartActivityViewModel() {
        App.getInstance().getAppComponent().inject(this);
    }

    private ArrayList<ScoutLaw> scoutLaws;

    public ArrayList<ScoutLaw> scoutLaws() {
        scoutLaws = repository.getScoutLaws();
        return scoutLaws;
    }
}
