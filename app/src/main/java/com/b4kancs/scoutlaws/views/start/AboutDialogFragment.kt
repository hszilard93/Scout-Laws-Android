package com.b4kancs.scoutlaws.views.start

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.databinding.LayoutAboutDialogBinding
import com.b4kancs.scoutlaws.setNewLocale
import javax.inject.Inject

/**
 * Created by hszilard on 12-Nov-18.
 */
class AboutDialogFragment : DialogFragment() {

    @Inject lateinit var repository: Repository

    private companion object {
        val LOG_TAG = AboutDialogFragment::class.simpleName
        var clickCounter = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<LayoutAboutDialogBinding>(inflater, R.layout.layout_about_dialog, null, false)

        binding.buttonAboutOk.setOnClickListener { dismiss() }
        binding.textAbout.setOnClickListener {
            clickCounter += 1
            Log.d(LOG_TAG, "Text field clicked $clickCounter times.")
            if (clickCounter == 5) {
                Log.i(LOG_TAG, "Changing locale to EN")
                setNewLocale(context!!, "en")
                repository.reloadScoutLaws()
                activity?.recreate()
            }
        }

        return binding.root
    }

    fun show(manager: FragmentManager) {
        Log.d(LOG_TAG, "Showing dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }
}