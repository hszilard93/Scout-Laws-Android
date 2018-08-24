package com.b4kancs.scoutlaws.views.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.services.NotificationScheduler
import com.b4kancs.scoutlaws.views.utils.areAnimationsEnabled
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

/**
 * Created by hszilard on 24-May-18.
 */
class PreferencesActivity : AppCompatActivity() {

    @Inject lateinit var notificationScheduler: NotificationScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ScoutLawApp.getInstance().applicationComponent.inject(this)

        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (supportFragmentManager.findFragmentById(R.id.frame_preferences_content) == null)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_preferences_content, PreferencesFragment())
                    .commit()

        PreferenceManager.getDefaultSharedPreferences(applicationContext).registerOnSharedPreferenceChangeListener {
            _: SharedPreferences, key: String ->
            when (key) {
                "pref_notification_timing_list", "pref_notification_preferred_time" ->
                    notificationScheduler.scheduleNotification(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                navigateUp()
                true
            }
            else -> false
        }
    }

    private fun navigateUp() {
        val upIntent = NavUtils.getParentActivityIntent(this)
        NavUtils.navigateUpTo(this, upIntent!!)
        if (areAnimationsEnabled(applicationContext))
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun onBackPressed() {
        navigateUp()
    }
}