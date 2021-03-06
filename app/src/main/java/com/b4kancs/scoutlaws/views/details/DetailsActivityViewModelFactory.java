package com.b4kancs.scoutlaws.views.details;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

/**
 * Created by hszilard on 21-Feb-18.
 * A ViewModelFactory is needed to initialize a ViewModel with parameters.
 */

class DetailsActivityViewModelFactory implements ViewModelProvider.Factory {
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
