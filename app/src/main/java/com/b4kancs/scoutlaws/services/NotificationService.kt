package com.b4kancs.scoutlaws.services

import android.app.job.JobParameters
import android.app.job.JobService
import com.b4kancs.scoutlaws.data.Repository
import java.util.*
import javax.inject.Inject

/**
 * Created by hszilard on 26-Jul-18.
 */
class NotificationService
@Inject constructor(val repository: Repository)
    : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        showQuizPromptNotification(applicationContext)
        repository.setLastNotificationShownAt(Calendar.getInstance().timeInMillis)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}