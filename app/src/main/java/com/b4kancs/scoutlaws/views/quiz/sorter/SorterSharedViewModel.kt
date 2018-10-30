package com.b4kancs.scoutlaws.views.quiz.sorter

import android.util.Log
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel
import java.util.*

/**
 * Created by hszilard on 04-Oct-18.
 */
class SorterSharedViewModel : AbstractSharedViewModel() {
    companion object {
        const val NUMBER_OF_OPTIONS = 3
        private val LOG_TAG = SorterSharedViewModel::class.simpleName
    }

    val scoutLaws = repository.laws

    override fun nextLawIndex(): Int {
        Log.d(LOG_TAG, "Generating next law index.")
        val random = Random()
        var index: Int
        do {
            index = random.nextInt(repository.numberOfScoutLaws - NUMBER_OF_OPTIONS + 1)
        } while (usedLaws.contains(index))
        usedLaws.add(index)
        return index
    }

    override fun getBestTime(): Long {
        return repository.getBestSorterTime()
    }

    override fun saveNewBestTime() {
        repository.setBestSorterTime(timeSpent)
    }
}