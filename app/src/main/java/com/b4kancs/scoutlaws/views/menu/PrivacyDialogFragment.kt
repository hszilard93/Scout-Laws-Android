package com.b4kancs.scoutlaws.views.menu

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil.inflate
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.databinding.LayoutPrivacyDialogBinding
import com.b4kancs.scoutlaws.databinding.LayoutPrivacyDialogLegacyBinding
import com.b4kancs.scoutlaws.logger.Logger
import javax.inject.Inject

/**
 * Created by b4kan on 06-May-19.
 */
class PrivacyDialogFragment : androidx.fragment.app.DialogFragment() {

    @Inject lateinit var repository: Repository

    private companion object {
        val LOG_TAG = PrivacyDialogFragment::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.log(Log.INFO, LOG_TAG, "onCreate(..)")
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.log(Log.DEBUG, LOG_TAG, "onCreateView(..)")

        // DialogFragment displayed incorrectly on APIs 21 and 22
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dialog?.requestWindowFeature(Window.FEATURE_LEFT_ICON)
            val binding = inflate<LayoutPrivacyDialogLegacyBinding>(inflater, R.layout.layout_privacy_dialog_legacy, null, false)
            binding.buttonPrivacyOk.setOnClickListener(buttonOkOnClickListener)
            binding.root
        } else {
            val binding = inflate<LayoutPrivacyDialogBinding>(inflater, R.layout.layout_privacy_dialog, null, false)
            binding.buttonPrivacyOk.setOnClickListener(buttonOkOnClickListener)
            binding.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            dialog?.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_info_primary_36dp)
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        Logger.log(Log.INFO, LOG_TAG, "show(..); Showing dialog")

        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }

    private val buttonOkOnClickListener = View.OnClickListener { dismiss() }
}
