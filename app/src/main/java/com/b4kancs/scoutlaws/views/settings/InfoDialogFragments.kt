package com.b4kancs.scoutlaws.views.settings

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.Log
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.data.Repository
import javax.inject.Inject

/**
 * Created by hszilard on 11-Nov-18.
 */
class BatteryOptimizerInfoDialogFragment : DialogFragment() {
    private companion object {
        val LOG_TAG = BatteryOptimizerInfoDialogFragment::class.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val builder = AlertDialog.Builder(activity!!)
        builder.setIcon(R.drawable.ic_warning_24dp)
                .setTitle(R.string.battery_optimizer_info_dialog_title)
                .setMessage(R.string.battery_optimizer_info_dialog_text)
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button) { _, _ -> dismiss() }
        return builder.create()
    }

    fun show(manager: FragmentManager) {
        Log.d(LOG_TAG, "Showing battery optimizer info dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }
}

class ResetInfoDialogFragment : DialogFragment() {
    private companion object {
        val LOG_TAG = ResetInfoDialogFragment::class.simpleName
    }

    @Inject lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val builder = AlertDialog.Builder(activity!!)
        builder.setIcon(R.drawable.ic_warning_24dp)
                .setTitle(R.string.pref_reset_stats_dialog_title)
                .setMessage(R.string.pref_reset_stats_dialog_text)
                .setPositiveButton(R.string.yes_button) { _, _ ->
                    Log.i(LOG_TAG, "Resetting user data.")
                    repository.resetUserData()
                    dismiss()
                }
                .setNegativeButton(R.string.cancel_button) {_, _ -> dismiss() }
        return builder.create()
    }

    fun show(manager: FragmentManager) {
        Log.d(LOG_TAG, "Showing battery optimizer info dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }
}
