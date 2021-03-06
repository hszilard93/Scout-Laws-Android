package com.b4kancs.scoutlaws.views.utils

import android.content.Context
import android.os.Vibrator
import android.preference.PreferenceManager
import android.util.Log.DEBUG
import com.b4kancs.scoutlaws.logger.Logger.Companion.log

/**
 * Created by hszilard on 14-Apr-18.
 */
private const val LOG_TAG = "CommonUtils"

fun vibrate(context: Context, duration: Long) {
    log(DEBUG, LOG_TAG, "Attempting to vibrate.")

    if (isVibrationEnabled(context)) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.vibrate(duration)
    }
}

fun isVibrationEnabled(context: Context): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getBoolean("pref_vibrate", true)
}

fun areAnimationsEnabled(context: Context): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getBoolean("pref_animate", true)
}

fun isPastelEnabled(context: Context): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getBoolean("pref_pastel", false)
}

