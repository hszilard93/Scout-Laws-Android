package com.b4kancs.scoutlaws

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.INFO
import com.b4kancs.scoutlaws.services.NotificationScheduler
import com.b4kancs.scoutlaws.logger.Logger.Companion.log
import javax.inject.Inject

/**
 * Created by hszilard on 28-Nov-18.
 */
class ExternalAvailableReceiver : BroadcastReceiver() {
    private companion object {
        val LOG_TAG = ExternalAvailableReceiver::class.simpleName
    }

    @Inject lateinit var notificationScheduler: NotificationScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        log(INFO, LOG_TAG, "onReceive()")
        App.getInstance().appComponent.inject(this)

        notificationScheduler.schedule(false)
    }
}
