package com.b4kancs.scoutlaws.views.quiz.sorter

import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel

/**
 * Created by hszilard on 04-Oct-18.
 */
class SorterSharedViewModel : AbstractSharedViewModel() {
    val scoutLaws = repository.laws

    override fun getBestTime(): Long {
        return repository.getBestSorterTime()
    }

    override fun saveNewBestTime() {
        repository.setBestSorterTime(timeSpent)
    }
}