package com.b4kancs.scoutlaws.views.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.b4kancs.scoutlaws.R

/**
 * Created by hszilard on 24-May-18.
 */
class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preferences)
    }
}