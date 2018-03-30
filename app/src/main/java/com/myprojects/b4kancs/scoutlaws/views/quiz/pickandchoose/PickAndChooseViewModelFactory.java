package com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by hszilard on 21-Mar-18.
 */

public class PickAndChooseViewModelFactory implements ViewModelProvider.Factory {

    private final PickAndChooseSharedViewModel sharedViewModel;

    public PickAndChooseViewModelFactory(PickAndChooseSharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PickAndChooseViewModel.class))
            return (T) new PickAndChooseViewModel(sharedViewModel);
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
