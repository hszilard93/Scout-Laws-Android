package com.b4kancs.scoutlaws.views.quiz.sorter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import com.crashlytics.android.Crashlytics

/**
 * Created by hszilard on 04-Oct-18.
 */
class SorterViewModelFactory(val sharedViewModel: SorterSharedViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Crashlytics.log(Log.DEBUG,SorterViewModelFactory::class.simpleName, "create")

        if (modelClass.isAssignableFrom(SorterViewModel::class.java))
            return SorterViewModel(sharedViewModel) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}