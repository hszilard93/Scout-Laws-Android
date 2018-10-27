package com.b4kancs.scoutlaws.views.quiz.sorter

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by hszilard on 01-Sep-18.
 */
class SorterViewModel(private val shared: SorterSharedViewModel) : ViewModel() {

    private companion object {
        const val NUMBER_OF_OPTIONS = 3
        private val LOG_TAG = SorterViewModel::class.simpleName
    }

    enum class State { CHECKABLE, DONE }

    val observableState = ObservableField<State>(State.CHECKABLE)
    val sequence = ArrayList<ScoutLaw>()
    private val scoutLaws = shared.scoutLaws
    private var tries = 0

    init {
        startTurn()
    }

    private fun startTurn() {
        Log.d(LOG_TAG, "New sorter turn started.")
        shared.incTurnCount()

        // Get an index that will be the start of a sequence of 3 consecutive scout laws (e.g. *7*,8,9)
        val startIndex = Random().nextInt(shared.repository.numberOfScoutLaws - NUMBER_OF_OPTIONS)

        for (i in 0 until NUMBER_OF_OPTIONS)
            sequence.add(scoutLaws[startIndex + i])

        sequence.shuffle()
    }

    private fun evaluate() {
        tries += 1
        if (sequence.inOrder()) {
            Log.d(LOG_TAG, "The order is correct.")
            observableState.set(State.DONE)
        } else
            Log.d(LOG_TAG, "The order is incorrect.")
    }

}

fun ArrayList<ScoutLaw>.inOrder(): Boolean {
    for (i in 0 until this.size - 1) {
        if (this[i].number > this[i + 1].number)
            return false
    }
    return true
}
