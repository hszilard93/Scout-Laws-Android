package com.b4kancs.scoutlaws.views.settings

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.util.Log
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.views.AbstractCustomDialogFragment
import javax.inject.Inject

/**
 * Created by hszilard on 11-Nov-18.
 */

class BatteryOptimizerInfoDialogFragment : AbstractCustomDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        title = getString(R.string.battery_optimizer_info_dialog_title)
        message = getString(R.string.battery_optimizer_info_dialog_text)
        icon = resources.getDrawable(R.drawable.ic_warning_24dp)
        cancelable = false
        positiveButton = Pair(getString(R.string.ok_button), OnClickListener { _, _ -> dismiss() })

        return super.onCreateDialog(savedInstanceState)
    }
}

class ResetInfoDialogFragment : AbstractCustomDialogFragment() {
    private companion object {
        val LOG_TAG = ResetInfoDialogFragment::class.simpleName
    }

    @Inject lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        title = getString(R.string.pref_reset_stats_dialog_title)
        message = getString(R.string.pref_reset_stats_dialog_text)
        icon = resources.getDrawable(R.drawable.ic_warning_24dp)
        cancelable = false
        positiveButton = Pair(getString(R.string.yes_button),
                OnClickListener { _, _ ->
                    Log.i(LOG_TAG, "Resetting user data.")
                    repository.resetUserData()
                    dismiss()
                })
        negativeButton = Pair(getString(R.string.cancel_button), OnClickListener { _, _ -> dismiss() })
        return super.onCreateDialog(savedInstanceState)
    }
}

