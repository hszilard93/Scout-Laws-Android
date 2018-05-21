package com.b4kancs.scoutlaws.data

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hszilard on 15-Feb-18.
 * The repository currently handles loading the scout laws from the resources.
 */
@Singleton
class Repository @Inject
constructor(private val resources: Resources, private val preferences: SharedPreferences) {

    companion object {
        private val LOG_TAG = Repository::class.simpleName
        private const val TOTAL_SCORE_KEY = "TOTAL_SCORE"
        private const val TOTAL_POSSIBLE_SCORE_KEY = "TOTAL_POSSIBLE_SCORE"
        private const val BEST_TIME_MULTIPLE = "BEST_TIME_MULTIPLE"
        private const val BEST_TIME_PICK_CHOOSE = "BEST_TIME_PICK_CHOOSE"
    }

    val laws = ArrayList<ScoutLaw>(10)
    val pickAndChooseLaws = ArrayList<PickAndChooseScoutLaw>(10)
    var numberOfScoutLaws: Int = 0      // How many laws are there? This is not constant!
        private set
    var totalScore: Int = 0
        private set
    var totalPossibleScore: Int = 0
        private set
    var bestMultipleTime: Long = 0
        set(value) {
            field = value
            preferences.edit().putLong(BEST_TIME_MULTIPLE, value).apply()
        }
    var bestPickChooseTime: Long = 0
        set(value) {
            field = value
            preferences.edit().putLong(BEST_TIME_PICK_CHOOSE, value).apply()
        }

    init {
        Log.d(LOG_TAG, "Constructing Repository instance.")
        loadLaws()
        loadUserData()
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

    private fun loadUserData() {
        totalScore = preferences.getInt(TOTAL_SCORE_KEY, 0)
        totalPossibleScore = preferences.getInt(TOTAL_POSSIBLE_SCORE_KEY, 0)
        bestMultipleTime = preferences.getLong(BEST_TIME_MULTIPLE, 0)
        bestPickChooseTime = preferences.getLong(BEST_TIME_PICK_CHOOSE, 0)
    }

    fun resetSharedPreferences() {
        preferences.edit().clear().apply()
    }

    fun increaseTotalScoreBy(thisMuch: Int) {
        totalScore += thisMuch
        preferences.edit().putInt(TOTAL_SCORE_KEY, totalScore).apply()
    }

    fun increaseTotalPossibleScoreBy(thisMuch: Int) {
        totalPossibleScore += thisMuch
        preferences.edit().putInt(TOTAL_POSSIBLE_SCORE_KEY, totalPossibleScore).apply()
    }

    fun resetScore() {
        totalScore = 0
        totalPossibleScore = 0
        preferences.edit()
                .putInt(TOTAL_SCORE_KEY, totalScore)
                .putInt(TOTAL_POSSIBLE_SCORE_KEY, totalPossibleScore)
                .apply()
    }
}
