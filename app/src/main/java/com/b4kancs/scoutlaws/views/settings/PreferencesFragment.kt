package com.b4kancs.scoutlaws.views.settings

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.b4kancs.scoutlaws.R

/**
 * Created by hszilard on 24-May-18.
 */
class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        // Check if the preference is our custom TimePreference
        var dialogFragment: DialogFragment? = null
        if (preference is TimePreference)
            dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.key)
        // If its a TimePreference dialog, show it
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(this.fragmentManager, "android.support.v7.preference.PreferenceFragment.DIALOG")
        }
        else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}