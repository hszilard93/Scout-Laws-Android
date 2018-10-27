package com.b4kancs.scoutlaws.views.quiz.sorter

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 * Created by hszilard on 04-Oct-18.
 */
class SorterViewModelFactory(val sharedViewModel: SorterSharedViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SorterViewModel::class.java))
            return SorterViewModel(sharedViewModel) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}