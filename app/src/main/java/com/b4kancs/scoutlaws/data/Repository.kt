package com.b4kancs.scoutlaws.data

import android.content.res.Resources
import android.util.Log
import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import com.b4kancs.scoutlaws.data.store.SharedPreferencesUserDataStore.Companion.TOTAL_SCORE_KEY
import com.b4kancs.scoutlaws.data.store.UserDataStore
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hszilard on 15-Feb-18.
 * The repository currently handles loading the scout laws from the resources.
 */
@Singleton
class Repository
@Inject constructor(private val resources: Resources, private val userDataStore: UserDataStore) {

    companion object {
        private val LOG_TAG = Repository::class.simpleName
    }

    val laws = ArrayList<ScoutLaw>(10)
    val pickAndChooseLaws = ArrayList<PickAndChooseScoutLaw>(10)
    var numberOfScoutLaws: Int = 0      // How many laws are there? This is not constant!
        private set

    init {
        Log.d(LOG_TAG, "Constructing Repository instance.")
        loadLaws()
    }

    private fun loadLaws() {
        resources.apply {
            Log.d(LOG_TAG, "Loading scout laws.")
            val packageName = "com.b4kancs.scoutlaws"

            // Check how many scout laws are there
            numberOfScoutLaws = getInteger(getIdentifier("number_of_laws", "integer", packageName))
            Log.d(LOG_TAG, "The number of scout laws is $numberOfScoutLaws")

            /* Building the ScoutLaw and PickAndChooseScoutLaw objects by dynamically loading them
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
                laws.add(law)

                // Loading PickAndChooseScoutLaw objects
                val pickChooseText =
                        getString(getIdentifier("law_${i + 1}_pick", "string", packageName))
                val optionsArray =
                        getStringArray(getIdentifier("law_${i + 1}_pick_options", "array", packageName))
                val pickChooseOptions = ArrayList(Arrays.asList(*optionsArray))

                val pickAndChooseScoutLaw = PickAndChooseScoutLaw(law, pickChooseText, pickChooseOptions)
                pickAndChooseLaws.add(i, pickAndChooseScoutLaw)
            }
        }
    }

    fun resetUserData() {
        userDataStore.reset()
    }

    fun getTotalScore() = userDataStore.totalScore

    fun getTotalPossibleScore() = userDataStore.totalPossibleScore

    fun getBestMultipleTime() = userDataStore.bestMultipleTime

    fun getBestPickChooseTime() = userDataStore.bestPickChooseTime

    fun increaseTotalScoreBy(thisMuch: Int) {
        userDataStore.totalScore += thisMuch
    }

    fun increaseTotalPossibleScoreBy(thisMuch: Int) {
        userDataStore.totalPossibleScore += thisMuch
    }

    fun setBestMultipleTime(newTime: Long) {
        userDataStore.bestMultipleTime = newTime
    }

    fun setBestPickChooseTime(newTime: Long) {
        userDataStore.bestPickChooseTime = newTime
    }
}
