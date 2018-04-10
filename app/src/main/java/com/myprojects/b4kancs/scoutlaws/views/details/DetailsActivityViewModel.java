package com.myprojects.b4kancs.scoutlaws.views.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.myprojects.b4kancs.scoutlaws.ScoutLawApp;
import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import javax.inject.Inject;

/**
 * Created by hszilard on 21-Feb-18.
 */

public class DetailsActivityViewModel extends ViewModel {
    @Inject protected Repository repository;
    /* This flag helps us retain state after config changes */
    private final ObservableBoolean modern = new ObservableBoolean();
    private final int index;
    private ScoutLaw scoutLaw;

    DetailsActivityViewModel(int index) {
        this.index = index;
    }

    protected void init() {
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        this.scoutLaw = repository.getLaws().get(index - 1);
        modern.set(true);
    }

    public ScoutLaw scoutLaw() {
        return scoutLaw;
    }

    public ObservableBoolean isModern() {
        return modern;
    }

    public void setModern(boolean modern) {
        this.modern.set(modern);
    }
}
