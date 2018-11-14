package com.b4kancs.scoutlaws.views.settings

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.services.NotificationScheduler
import javax.inject.Inject

/**
 * Created by hszilard on 24-May-18.
 */
class PreferencesFragment : PreferenceFragmentCompat() {

    @Inject lateinit var notificationScheduler: NotificationScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext).registerOnSharedPreferenceChangeListener { preferences, key ->
            when (key) {
                "pref_notification_timing_list" -> {
                    val defaultTiming = if (activity?.resources != null)
                        activity?.resources!!.getStringArray(R.array.pref_notifications_list_values)[0] ?: "never"
                    else
                        "never"
                    val currentTiming = preferences.getString("pref_notification_timing_list", defaultTiming)
                    if (currentTiming != defaultTiming)
                        BatteryOptimizerInfoDialogFragment().show(activity!!.supportFragmentManager)

                    notificationScheduler.schedule(true)
                }
                "pref_notification_preferred_time" ->
                    notificationScheduler.schedule(true)
            }
        }

        val resetButton = preferenceManager.findPreference("pref_reset_stats")
        resetButton?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            ResetInfoDialogFragment().show(activity!!.supportFragmentManager)
            true
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        // Check if the preference is our custom TimePreference
        if (preference is TimePreference) {
            val dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.key)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(fragmentManager!!, "android.support.v7.preference.PreferenceFragment.DIALOG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}