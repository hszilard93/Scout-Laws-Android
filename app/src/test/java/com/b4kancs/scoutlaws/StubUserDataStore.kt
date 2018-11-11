package com.b4kancs.scoutlaws

import com.b4kancs.scoutlaws.data.store.UserDataStore

/**
 * Created by hszilard on 22-May-18.
 */
class StubUserDataStore : UserDataStore {
    override var totalScore: Int = 0
    override var totalPossibleScore: Int = 0
    override var bestMultipleTime: Long = 0L
    override var bestPickerTime: Long = 0L
    override var bestSorterTime: Long = 0L

    override fun reset() {
        totalScore = 0
        totalPossibleScore = 0
        bestMultipleTime = 0L
        bestPickerTime = 0L
        bestSorterTime = 0L
    }

}