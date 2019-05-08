package com.b4kancs.scoutlaws.views.quiz.sorter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.b4kancs.scoutlaws.logger.Logger

/**
 * Created by hszilard on 04-Oct-18.
 */
class SorterViewModelFactory(val sharedViewModel: SorterSharedViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Logger.log(Log.DEBUG,SorterViewModelFactory::class.simpleName, "create")

        if (modelClass.isAssignableFrom(SorterViewModel::class.java))
            return SorterViewModel(sharedViewModel) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
