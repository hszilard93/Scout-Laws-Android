package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by hszilard on 28-Feb-18.
 * Factory needed for instantiating ViewModel with arguments.
 */

class MultipleChoiceViewModelFactory implements ViewModelProvider.Factory {

    private final MultipleChoiceSharedViewModel sharedViewModel;

    MultipleChoiceViewModelFactory(@NonNull MultipleChoiceSharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MultipleChoiceViewModel.class)) {
            return (T) new MultipleChoiceViewModel(sharedViewModel);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
