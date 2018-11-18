package com.b4kancs.scoutlaws.views.quiz.sorter

import android.util.Log.DEBUG
import android.util.Log.INFO
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.crashlytics.android.Crashlytics.log

/**
 * Created by hszilard on 01-Sep-18.
 */
class SorterViewModel(private val shared: SorterSharedViewModel) : ViewModel() {
    private companion object {
        val LOG_TAG = SorterViewModel::class.simpleName
    }

    enum class State { CHECKABLE, DONE }

    val observableState = ObservableField<State>(State.CHECKABLE)
    val sequence = ArrayList<ScoutLaw>()
    private val scoutLaws = shared.scoutLaws
    private var tries = 0

    init {
        log(DEBUG, LOG_TAG, "init {}")
        observableState.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                log(INFO, LOG_TAG, "observableStateChangedTo: " + observableState.get())
            }
        })
        startTurn()
    }

    private fun startTurn() {
        log(INFO, LOG_TAG, "startTurn(); New sorter turn started.")
        shared.incTurnCount()
        // Get an index that will be the start of a sequence of 3 consecutive scout scoutLaws (e.g. *7*,8,9)
        val startIndex = shared.nextLawIndex()
        for (i in 0 until SorterSharedViewModel.NUMBER_OF_OPTIONS)
            sequence.add(scoutLaws[startIndex + i])
        sequence.shuffle()
    }

    fun evaluate(): Boolean {
        log(DEBUG, LOG_TAG, "evaluate()")
        return if (sequence.inOrder()) {
            log(INFO, LOG_TAG, "The order is correct.")
            observableState.set(State.DONE)
            if (tries == 0)
                shared.incScore()
            if (shared.isThisTheLastTurn)
                shared.finish()
            true
        } else {
            log(INFO, LOG_TAG, "The order is incorrect.")
            tries++
            false
        }
    }

    private fun ArrayList<ScoutLaw>.inOrder(): Boolean {
        for (i in 0 until this.size - 1) {
            if (this[i].number > this[i + 1].number)
                return false
        }
        return true
    }
}
