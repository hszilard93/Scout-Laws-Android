package com.b4kancs.scoutlaws.data.store

/**
 * Created by hszilard on 22-May-18.
 */
interface UserDataStore {
    var totalScore: Int
    var totalPossibleScore: Int
    var bestMultipleTime: Long
    var bestPickChooseTime: Long

    fun reset()
}