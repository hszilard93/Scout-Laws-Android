package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

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
