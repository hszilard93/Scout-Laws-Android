package com.b4kancs.scoutlaws.views.settings

import android.os.Bundle
import android.util.Log.INFO
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.BaseActivity
import com.b4kancs.scoutlaws.views.utils.areAnimationsEnabled
import com.b4kancs.scoutlaws.logger.Logger.Companion.log
import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Created by hszilard on 24-May-18.
 */
class PreferencesActivity : BaseActivity() {

    private companion object {
        val LOG_TAG = PreferencesActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log(INFO, LOG_TAG, "onCreate()")
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
                log(INFO, LOG_TAG, "Home clicked.")
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
        log(INFO, LOG_TAG, "Back pressed.")
        navigateUp()
    }
}
