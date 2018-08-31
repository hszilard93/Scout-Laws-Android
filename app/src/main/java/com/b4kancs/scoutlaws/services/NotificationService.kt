package com.b4kancs.scoutlaws.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.preference.PreferenceManager
import android.util.Log
import com.b4kancs.scoutlaws.ScoutLawApp
import com.b4kancs.scoutlaws.data.Repository
import java.util.*
import javax.inject.Inject

/**
 * Created by hszilard on 26-Jul-18.
 */
class NotificationService: JobService() {
    companion object { private val LOG_TAG = NotificationService::class.java.simpleName }

    @Inject lateinit var repository: Repository

    init {
        ScoutLawApp.getInstance().applicationComponent.inject(this)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(LOG_TAG, "onStartJob")

        showQuizPromptNotification(applicationContext)
        repository.setLastNotificationShownAt(Calendar.getInstance().timeInMillis)
        // Schedule next notification
        NotificationScheduler(applicationContext, repository, PreferenceManager.getDefaultSharedPreferences(applicationContext))
                .schedule(false)

        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}