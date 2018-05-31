package com.b4kancs.scoutlaws.views.utils

import android.content.Context
import android.os.Vibrator
import android.preference.PreferenceManager
import android.util.Log

/**
 * Created by hszilard on 14-Apr-18.
 */
private const val LOG_TAG = "CommonUtils"

fun vibrate(context: Context, duration: Long) {
    Log.d(LOG_TAG, "Attempting to vibrate.")

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

