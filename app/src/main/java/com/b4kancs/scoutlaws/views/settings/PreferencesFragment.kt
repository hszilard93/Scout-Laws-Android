package com.b4kancs.scoutlaws.views.settings

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log.INFO
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.logger.CrashlyticsLogger
import com.b4kancs.scoutlaws.logger.DefaultLogger
import com.b4kancs.scoutlaws.logger.Logger
import com.b4kancs.scoutlaws.logger.Logger.Companion.log
import com.b4kancs.scoutlaws.services.NotificationScheduler
import javax.inject.Inject

/**
 * Created by hszilard on 24-May-18.
 */
class PreferencesFragment : PreferenceFragmentCompat() {

    private companion object {
        val LOG_TAG = PreferencesFragment::class.simpleName
    }

    @Inject lateinit var notificationScheduler: NotificationScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        log(INFO, LOG_TAG, "onCreate(..)")
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        log(INFO, LOG_TAG, "onCreatePreferences(..)")

        addPreferencesFromResource(R.xml.preferences)

        PreferenceManager
                .getDefaultSharedPreferences(activity!!.applicationContext)
                .registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)

        val resetButton = preferenceManager.findPreference("pref_reset_stats")
        resetButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            log(INFO, LOG_TAG, "Showing reset info dialog.")
            ResetInfoDialogFragment().show(activity!!.supportFragmentManager)
            true
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        // Check if the preference is our custom TimePreference
        if (preference is TimePreference) {
            log(INFO, LOG_TAG, "Showing time preference dialog.")
            val dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.key)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(fragmentManager!!, "android.support.v7.preference.PreferenceFragment.DIALOG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private val onSharedPreferenceChangeListener = OnSharedPreferenceChangeListener { preferences, key ->
        preferences.apply {
            when (key) {
                "pref_pastel" ->
                    log(INFO, LOG_TAG, "Pastel Colors Enabled changed: ${getBoolean(key, false)}")
                "pref_animate" ->
                    log(INFO, LOG_TAG, "Animations Enabled changed: ${getBoolean(key, false)}")
                "pref_vibrate" ->
                    log(INFO, LOG_TAG, "Vibrations Enabled changed: ${getBoolean(key, false)}")
                "pref_crash_reports" -> {
                    val isCrashReportingEnabled = getBoolean(key, false)
                    log(INFO, LOG_TAG, "Crash Reporting Enabled changed: $isCrashReportingEnabled")
                    Logger.logger = if (isCrashReportingEnabled) CrashlyticsLogger() else DefaultLogger()
                }
                "pref_notification_timing_list" -> {
                    val defaultTiming = if (activity?.resources != null)
                        activity?.resources!!.getStringArray(R.array.pref_notifications_list_values)[0] ?: "never"
                    else
                        "never"

                    val currentTiming = getString("pref_notification_timing_list", defaultTiming)
                    if (currentTiming != defaultTiming) {
                        log(INFO, LOG_TAG, "Showing battery optimizer dialog.")
                        BatteryOptimizerInfoDialogFragment().show(activity?.supportFragmentManager)
                    }

                    notificationScheduler.schedule(true)
                }
                "pref_notification_preferred_time" ->
                    notificationScheduler.schedule(true)
            }
        }
    }
}
