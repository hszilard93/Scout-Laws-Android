package com.b4kancs.scoutlaws.views.quiz.sorter

import android.util.Log
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel
import com.crashlytics.android.Crashlytics
import java.util.*

/**
 * Created by hszilard on 04-Oct-18.
 */
class SorterSharedViewModel : AbstractSharedViewModel() {
    companion object {
        const val NUMBER_OF_OPTIONS = 3
        private val LOG_TAG = SorterSharedViewModel::class.simpleName
    }

    val scoutLaws = repository.scoutLaws

    override fun nextLawIndex(): Int {
        Crashlytics.log(Log.DEBUG, LOG_TAG, "nextLawIndex()")
        val random = Random()
        var index: Int
        do {
            index = random.nextInt(repository.numberOfScoutLaws - NUMBER_OF_OPTIONS + 1)
        } while (usedLaws.contains(index))
        usedLaws.add(index)
        Crashlytics.log(Log.DEBUG, LOG_TAG, "returning index: $index")
        return index
    }

    override fun getBestTime(): Long = repository.getBestSorterTime()

    override fun saveNewBestTime() {
        Crashlytics.log(Log.INFO, LOG_TAG, "saveNewBestTime(); time = $timeSpent")
        repository.setBestSorterTime(timeSpent)
    }
}