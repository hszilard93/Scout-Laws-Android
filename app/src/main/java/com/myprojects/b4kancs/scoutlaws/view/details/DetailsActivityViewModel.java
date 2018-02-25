package com.myprojects.b4kancs.scoutlaws.view.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

/**
 * Created by hszilard on 21-Feb-18.
 */

public class DetailsActivityViewModel extends AndroidViewModel {
    private final Repository repository;
    private final ScoutLaw scoutLaw;
    /* This flag helps us retain state after config changes */
    private final ObservableBoolean modern = new ObservableBoolean();

    public DetailsActivityViewModel(@NonNull Application application, int index) {
        super(application);
        repository = Repository.getInstance(application);
        this.scoutLaw = repository.getLaws().get(index);
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
