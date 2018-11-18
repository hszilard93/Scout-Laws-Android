package com.b4kancs.scoutlaws.data.store

/**
 * Created by hszilard on 22-May-18.
 */
interface UserDataStore {
    var totalScore: Int
    var totalPossibleScore: Int
    // Times are in milliseconds
    var bestMultipleTime: Long
    var bestPickerTime: Long
    var bestSorterTime: Long

    fun reset()
}