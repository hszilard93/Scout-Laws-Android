package com.b4kancs.scoutlaws.data.store

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Created by hszilard on 22-May-18.
 */
class SharedPreferencesUserDataStore
@Inject constructor(private val preferences: SharedPreferences)
    : UserDataStore
{
    companion object {
        const val TOTAL_SCORE_KEY = "TOTAL_SCORE"
        const val TOTAL_POSSIBLE_SCORE_KEY = "TOTAL_POSSIBLE_SCORE"
        const val BEST_TIME_MULTIPLE = "BEST_TIME_MULTIPLE"
        const val BEST_TIME_PICKER = "BEST_TIME_PICKER"
        const val BEST_TIME_SORTER = "BEST_TIME_SORTER"
    }

    override var totalScore: Int = 0
        get() {
            if (field == 0)
                field = preferences.getInt(TOTAL_SCORE_KEY, 0)
            return field
        }
        set(value) {
            field = value
            preferences.edit().putInt(TOTAL_SCORE_KEY, value).apply()
        }
    override var totalPossibleScore: Int = 0
        get() {
            if (field == 0)
                field = preferences.getInt(TOTAL_POSSIBLE_SCORE_KEY, 0)
            return field
        }
        set(value) {
            field = value
            preferences.edit().putInt(TOTAL_POSSIBLE_SCORE_KEY, value).apply()
        }
    override var bestMultipleTime: Long = 0L
        get() {
            if (field == 0L)
                field = preferences.getLong(BEST_TIME_MULTIPLE, 0)
            return field
        }
        set(value) {
            field = value
            preferences.edit().putLong(BEST_TIME_MULTIPLE, value).apply()
        }

    override var bestPickerTime: Long = 0L
        get() {
            if (field == 0L)
                field = preferences.getLong(BEST_TIME_PICKER, 0)
            return field
        }
        set(value) {
            field = value
            preferences.edit().putLong(BEST_TIME_PICKER, value).apply()
        }

    override var bestSorterTime: Long = 0L
        get() {
            if (field == 0L)
                field = preferences.getLong(BEST_TIME_SORTER, 0)
            return field
        }
        set(value) {
            field = value
            preferences.edit().putLong(BEST_TIME_SORTER, value).apply()
        }

    override fun reset() {
        preferences.edit().clear().apply()
    }
}