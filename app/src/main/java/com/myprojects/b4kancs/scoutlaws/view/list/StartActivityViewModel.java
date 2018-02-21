package com.myprojects.b4kancs.scoutlaws.view.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

import java.util.ArrayList;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class StartActivityViewModel extends AndroidViewModel {
    private Repository repository;
    private ArrayList<ScoutLaw> scoutLaws;

    public StartActivityViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public ArrayList<ScoutLaw> scoutLaws() {
        if (scoutLaws == null)
            scoutLaws = repository.getLaws();
        return scoutLaws;
    }

}
