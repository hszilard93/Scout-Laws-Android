package com.b4kancs.scoutlaws.views.start

import android.os.Bundle
import android.util.Log.DEBUG
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.databinding.LayoutAboutDialogBinding
import com.b4kancs.scoutlaws.services.NotificationService
import com.b4kancs.scoutlaws.services.showQuizPromptNotification
import com.b4kancs.scoutlaws.setNewLocale
import com.crashlytics.android.Crashlytics.log
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
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log(DEBUG, LOG_TAG, "onCreateView(..)")
        val binding = DataBindingUtil.inflate<LayoutAboutDialogBinding>(inflater, R.layout.layout_about_dialog, null, false)

        binding.buttonAboutOk.setOnClickListener { dismiss() }
        binding.textAbout.setOnClickListener {
            clickCounter += 1
            log(DEBUG, LOG_TAG, "Text field clicked $clickCounter times.")
            if (clickCounter == 5) {
                log(INFO, LOG_TAG, "Changing locale to EN")
                setNewLocale(context!!, "en")
                repository.reloadScoutLaws()
                activity?.recreate()
            }
        }
        // TODO: Away
        binding.iconAbout.setOnClickListener {
            showQuizPromptNotification(context!!)
        }

        return binding.root
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        log(INFO, LOG_TAG, "Showing dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }
}