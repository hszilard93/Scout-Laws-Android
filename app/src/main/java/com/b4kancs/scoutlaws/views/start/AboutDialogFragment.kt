package com.b4kancs.scoutlaws.views.start

import android.os.Build
import android.os.Bundle
import android.util.Log.DEBUG
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.databinding.LayoutAboutDialogBinding
import com.b4kancs.scoutlaws.databinding.LayoutAboutDialogLegacyBinding
import com.b4kancs.scoutlaws.getBaseContextWithLocale
import com.b4kancs.scoutlaws.logger.Logger.Companion.log
import javax.inject.Inject

/**
 * Created by hszilard on 12-Nov-18.
 */
class AboutDialogFragment : androidx.fragment.app.DialogFragment() {

    @Inject lateinit var repository: Repository

    private companion object {
        val LOG_TAG = AboutDialogFragment::class.simpleName
        var clickCounter = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log(INFO, LOG_TAG, "onCreate(..)")
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log(DEBUG, LOG_TAG, "onCreateView(..)")

        // DialogFragment displayed incorrectly on APIs 21 and 22
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dialog?.requestWindowFeature(Window.FEATURE_LEFT_ICON)
            val binding = DataBindingUtil.inflate<LayoutAboutDialogLegacyBinding>(inflater, R.layout.layout_about_dialog_legacy, null, false)
            binding.buttonAboutOk.setOnClickListener(buttonOkOnClickListener)
            binding.textAbout.setOnClickListener(textAboutOnClickListener)
            return binding.root
        } else {
            val binding = DataBindingUtil.inflate<LayoutAboutDialogBinding>(inflater, R.layout.layout_about_dialog, null, false)
            binding.buttonAboutOk.setOnClickListener(buttonOkOnClickListener)
            binding.textAbout.setOnClickListener(textAboutOnClickListener)
            return binding.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            dialog?.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_info_primary_36dp)
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        log(INFO, LOG_TAG, "show(..); Showing dialog")

        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }

    private val textAboutOnClickListener = View.OnClickListener {
        clickCounter += 1
        log(DEBUG, LOG_TAG, "Text field clicked $clickCounter times.")
        if (clickCounter == 5) {
            log(INFO, LOG_TAG, "Changing locale to EN")
            getBaseContextWithLocale(context!!, "en")
            repository.reloadScoutLaws()
            activity?.recreate()
        }
    }

    private val buttonOkOnClickListener = View.OnClickListener { dismiss() }
}
