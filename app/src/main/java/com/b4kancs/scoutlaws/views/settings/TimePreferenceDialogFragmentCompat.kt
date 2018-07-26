package com.b4kancs.scoutlaws.views.settings

import android.os.Build
import android.os.Bundle
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import com.b4kancs.scoutlaws.R

/**
 * Created by hszilard on 17-Jul-18.
 * Based on https://github.com/jakobulbrich/preferences-demo/blob/master/app/src/main/java/de/jakobulbrich/preferences/TimePreferenceDialogFragmentCompat.java
 */
class TimePreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    private var timePicker: TimePicker? = null

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        timePicker = view.findViewById<View>(R.id.time_pref) as TimePicker

        // Exception: There is no TimePicker with the id 'time_pref' in the dialog.
        if (timePicker == null) {
            throw IllegalStateException("Dialog view must contain a TimePicker with id 'time_pref'")
        }

        // Get the time from the related Preference
        var minutesAfterMidnight: Int? = null
        val preference = preference
        if (preference is TimePreference) {
            minutesAfterMidnight = preference.time
        }

        // Set the time to the TimePicker
        if (minutesAfterMidnight != null) {
            val hours = minutesAfterMidnight / 60
            val minutes = minutesAfterMidnight % 60
            val is24hour = DateFormat.is24HourFormat(context)

            timePicker!!.setIs24HourView(is24hour)
            timePicker!!.currentHour = hours
            timePicker!!.currentMinute = minutes
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            // Get the current values from the TimePicker
            val hours: Int
            val minutes: Int
            if (Build.VERSION.SDK_INT >= 23) {
                hours = timePicker!!.hour
                minutes = timePicker!!.minute
            } else {
                hours = timePicker!!.currentHour
                minutes = timePicker!!.currentMinute
            }

            val minutesAfterMidnight = hours * 60 + minutes

            val preference = preference
            if (preference is TimePreference) {
                // This allows the client to ignore the user value
                if (preference.callChangeListener(minutesAfterMidnight)) {
                    // Save the value
                    preference.time = minutesAfterMidnight
                }
            }
        }
    }

    companion object {
        /**
         * Creates a new Instance of the TimePreferenceDialogFragment and stores the key of the
         * related Preference
         */
        fun newInstance(key: String): TimePreferenceDialogFragmentCompat {
            val fragment = TimePreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(PreferenceDialogFragmentCompat.ARG_KEY, key)
            fragment.arguments = b

            return fragment
        }
    }
}