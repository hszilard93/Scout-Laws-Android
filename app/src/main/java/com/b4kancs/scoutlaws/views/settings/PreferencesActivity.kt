package com.b4kancs.scoutlaws.views.settings

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.views.utils.areAnimationsEnabled
import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Created by hszilard on 24-May-18.
 */
class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (supportFragmentManager.findFragmentById(R.id.frame_preferences_content) == null)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_preferences_content, PreferencesFragment())
                    .commit()
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
