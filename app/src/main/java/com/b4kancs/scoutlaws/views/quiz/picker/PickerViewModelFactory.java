package com.b4kancs.scoutlaws.views.quiz.picker;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by hszilard on 21-Mar-18.
 */

class PickerViewModelFactory implements ViewModelProvider.Factory {
    private final PickerSharedViewModel sharedViewModel;

    PickerViewModelFactory(PickerSharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PickerViewModel.class))
            return (T) new PickerViewModel(sharedViewModel);
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
