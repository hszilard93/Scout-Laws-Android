package com.myprojects.b4kancs.scoutlaws.views.details;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by hszilard on 21-Feb-18.
 * A ViewModelFactory is needed to initialize a ViewModel with parameters.
 */

public class DetailsActivityViewModelFactory implements ViewModelProvider.Factory {
    private final int index;

    DetailsActivityViewModelFactory(int index) {
        this.index = index;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsActivityViewModel(index);
    }
}
