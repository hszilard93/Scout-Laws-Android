package com.b4kancs.scoutlaws.views.menu

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.b4kancs.scoutlaws.App
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.data.Repository
import com.b4kancs.scoutlaws.databinding.LayoutStatsDialogBinding
import com.b4kancs.scoutlaws.databinding.LayoutStatsDialogLegacyBinding
import com.b4kancs.scoutlaws.logger.Logger
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
        Logger.log(Log.INFO, LOG_TAG, "onCreate(..)")
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.log(Log.DEBUG, LOG_TAG, "onCreateView(..)")

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dialog?.requestWindowFeature(Window.FEATURE_LEFT_ICON)
            val binding = DataBindingUtil.inflate<LayoutStatsDialogLegacyBinding>(inflater, R.layout.layout_stats_dialog_legacy, null, false)
            binding.repository = repository
            binding.buttonStatsOk.setOnClickListener(buttonOkOnClickListener)
            binding.root
        } else {
            val binding = DataBindingUtil.inflate<LayoutStatsDialogBinding>(inflater, R.layout.layout_stats_dialog, null, false)
            binding.repository = repository
            binding.buttonStatsOk.setOnClickListener(buttonOkOnClickListener)
            binding.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            dialog?.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_sort_primary_36dp)
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        Logger.log(Log.INFO, LOG_TAG, "show(..); Showing dialog")
        val transaction = manager.beginTransaction()
        transaction.add(this, tag)
        transaction.commitAllowingStateLoss()
    }

    private val buttonOkOnClickListener = View.OnClickListener { dismiss() }
}
