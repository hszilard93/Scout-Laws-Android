package com.b4kancs.scoutlaws.data

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log.DEBUG
import android.util.Log.INFO
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.data.model.PickerScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.b4kancs.scoutlaws.data.store.UserDataStore
import com.b4kancs.scoutlaws.logger.Logger.Companion.log
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by hszilard on 15-Feb-18.
 * The repository currently handles loading the scout scoutLaws from the resources.
 */
@Singleton
class Repository
@Inject constructor(private val resources: Resources,
                    private val userDataStore: UserDataStore,
                    @Named("default_preferences") private val sharedPreferences: SharedPreferences) {

    companion object {
        private val LOG_TAG = Repository::class.simpleName
        const val LAST_NOTIFICATION_TIME_KEY = "LAST_NOTIFICATION_TIME_KEY"
    }

    lateinit var scoutLaws: ArrayList<ScoutLaw>
    lateinit var pickerScoutLaws: ArrayList<PickerScoutLaw>
    var numberOfScoutLaws: Int = 0      // How many scoutLaws are there? This is not constant!
        private set

    init {
        log(INFO, LOG_TAG, "init")
        loadScoutLaws()
    }

    private fun loadScoutLaws() {
        resources.apply {
            log(INFO, LOG_TAG, "loadScoutlaws()")
            val packageName = "com.b4kancs.scoutlaws"

            // Check how many scout scoutLaws are there
            numberOfScoutLaws = getInteger(getIdentifier("number_of_laws", "integer", packageName))
            log(DEBUG, LOG_TAG, "The number of scout scoutLaws is $numberOfScoutLaws")

            scoutLaws = ArrayList(numberOfScoutLaws)
            pickerScoutLaws = ArrayList(numberOfScoutLaws)

            /* Building the ScoutLaw and PickerScoutLaw objects by dynamically loading them
             * from their resource files by constructing their names */
            for (i in 0 until numberOfScoutLaws) {
                // ScoutLaw objects
                val text =
                        getString(getIdentifier("law_${i + 1}", "string", packageName))
                val desc =
                        getString(getIdentifier("law_${i + 1}_desc", "string", packageName))
                val origDesc =
                        getString(getIdentifier("law_${i + 1}_desc_orig", "string", packageName))
                val law = ScoutLaw(i + 1, text, desc, origDesc)
                scoutLaws.add(law)

                // Loading PickerScoutLaw objects
                val pickerText =
                        getString(getIdentifier("law_${i + 1}_pick", "string", packageName))
                val optionsArray =
                        getStringArray(getIdentifier("law_${i + 1}_pick_options", "array", packageName))
                val pickerOptions = ArrayList(Arrays.asList(*optionsArray))

                val pickerScoutLaw = PickerScoutLaw(law, pickerText, pickerOptions)
                pickerScoutLaws.add(i, pickerScoutLaw)
            }
        }
    }

    fun reloadScoutLaws() {
        log(DEBUG, LOG_TAG, "reloadScoutLaws()")
        loadScoutLaws()
    }

    fun resetUserData() {
        log(DEBUG, LOG_TAG, "resetUserData()")
        userDataStore.reset()
    }

    fun getTotalScore() = userDataStore.totalScore

    fun getTotalPossibleScore() = userDataStore.totalPossibleScore

    fun getBestMultipleTime() = userDataStore.bestMultipleTime

    fun getBestPickerTime() = userDataStore.bestPickerTime

    fun getBestSorterTime() = userDataStore.bestSorterTime

    fun getLastNotificationShownAt() = sharedPreferences.getLong(LAST_NOTIFICATION_TIME_KEY, 0)

    fun increaseTotalScoreBy(thisMuch: Int) {
        log(DEBUG, LOG_TAG, "increaseTotalScoreBy($thisMuch)")
        userDataStore.totalScore += thisMuch
    }

    fun increaseTotalPossibleScoreBy(thisMuch: Int) {
        log(DEBUG, LOG_TAG, "increaseTotalPossibleScoreBy($thisMuch)")
        userDataStore.totalPossibleScore += thisMuch
    }

    fun setBestMultipleTime(newTime: Long) {
        log(DEBUG, LOG_TAG, "setBestMultipleTime($newTime)")
        userDataStore.bestMultipleTime = newTime
    }

    fun setBestPickerTime(newTime: Long) {
        log(DEBUG, LOG_TAG, "setBestPickerTime($newTime)")
        userDataStore.bestPickerTime = newTime
    }

    fun setBestSorterTime(newTime: Long) {
        log(DEBUG, LOG_TAG, "setBestSorterTime($newTime)")
        userDataStore.bestSorterTime = newTime
    }

    fun setLastNotificationShownAt(time: Long) {
        log(DEBUG, LOG_TAG, "setLastNotificationShownAt($time)")
        sharedPreferences.edit().putLong(LAST_NOTIFICATION_TIME_KEY, time).apply()
    }
}
