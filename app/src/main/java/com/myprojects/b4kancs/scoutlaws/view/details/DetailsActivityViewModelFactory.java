package com.myprojects.b4kancs.scoutlaws.view.details;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by hszilard on 21-Feb-18.
 */

public class DetailsActivityViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private Application application;
    private int index;

    public DetailsActivityViewModelFactory(@NonNull Application application, int index) {
        super(application);
        this.application = application;
        this.index = index;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsActivityViewModel(application, index);
    }
}
