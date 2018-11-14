package com.b4kancs.scoutlaws.views

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import android.util.Log

/**
 * Created by hszilard on 12-Nov-18.
 */
abstract class AbstractCustomDialogFragment : androidx.fragment.app.DialogFragment() {
    var title: String? = null
    var message: String? = null
    var icon: Drawable? = null
    var cancelable: Boolean? = null
    var positiveButton: Pair<String, DialogInterface.OnClickListener>? = null
    var negativeButton: Pair<String, DialogInterface.OnClickListener>? = null
    var neutralButton: Pair<String, DialogInterface.OnClickListener>? = null

    /** Override this method in descendants to set up the properties you want
     *  then you can call this implementation. Avoids state errors. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = cancelable ?: true
        val builder = AlertDialog.Builder(activity!!)
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton?.first, positiveButton?.second)
                .setNegativeButton(negativeButton?.first, negativeButton?.second)
                .setNeutralButton(neutralButton?.first, neutralButton?.second)
        return builder.create()
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        Log.d(this::class.simpleName, "Showing dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }
}