package com.b4kancs.scoutlaws.views.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.databinding.LayoutStatsDialogBinding
import com.crashlytics.android.Crashlytics
import javax.inject.Inject

/**
 * Created by hszilard on 16-Nov-18.
 */
class StatsDialogFragment : DialogFragment() {

    @Inject lateinit var repository: Repository

    private companion object {
        val LOG_TAG = StatsDialogFragment::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Crashlytics.log(Log.INFO, LOG_TAG, "onCreate(..)")
        super.onCreate(savedInstanceState)
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Crashlytics.log(Log.DEBUG, LOG_TAG, "onCreateView(..)")

        val binding = DataBindingUtil.inflate<LayoutStatsDialogBinding>(inflater, R.layout.layout_stats_dialog, null, false)
        binding.repository = repository
        binding.buttonStatsOk.setOnClickListener { dismiss() }

        return binding.root
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        Crashlytics.log(Log.INFO, LOG_TAG, "show(..); Showing dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }
}