package com.myprojects.b4kancs.scoutlaws.view.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

/**
 * Created by hszilard on 21-Feb-18.
 */

public class DetailsActivityViewModel extends AndroidViewModel {
    private Repository repository;
    private ScoutLaw scoutLaw;

    public DetailsActivityViewModel(@NonNull Application application, int index) {
        super(application);
        repository = Repository.getInstance(application);
        this.scoutLaw = repository.getLaws().get(index);
    }

    public ScoutLaw scoutLaw() {
        return scoutLaw;
    }
}
