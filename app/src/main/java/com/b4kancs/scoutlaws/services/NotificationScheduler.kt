package com.b4kancs.scoutlaws.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.PersistableBundle
import android.util.Log
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.data.Repository
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hszilard on 16-Jul-18.
 */
@Singleton
class NotificationScheduler
@Inject constructor(
        val context: Context,
        val repository: Repository,
        @Named("default_preferences") val sharedPreferences: SharedPreferences) {

    companion object {
        private const val NOTIFICATION_JOB_ID = 1
        private const val KEY_NOTIFICATION_TIME = "KEY_NOTIFICATION_TIME"
        private val LOG_TAG = NotificationScheduler::class.simpleName
    }

    fun schedule(forced: Boolean): Long {
        Log.d(LOG_TAG, "scheduleNotification called. forced = $forced")

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        if (!forced) {
            // Check if we already have a job scheduled
            jobScheduler.allPendingJobs.forEach {
                if (it.id == NOTIFICATION_JOB_ID) {
                    /* If a notification would be shown within the hour, or it is in the past, reschedule it.
                     * (No need to prompt the user when he's just used/is using the app.) */
                    if (it.extras.getLong(KEY_NOTIFICATION_TIME) - Calendar.getInstance().timeInMillis <= 60 * 60 * 1000) {
                        Log.d(LOG_TAG, "Rescheduling notification because it is too close in time.")
                    } else {
                        val relativeNotificationTimeInMinutes = (it.extras.getLong(KEY_NOTIFICATION_TIME)
                                - Calendar.getInstance().timeInMillis) / 1000 / 60
                        Log.d(LOG_TAG, "Notification is already scheduled for around " +
                                "$relativeNotificationTimeInMinutes minutes from now.")
                        return -1
                    }
                }
            }
        } else {
            // Cancel potentially existing jobs
            Log.d(LOG_TAG, "Forced. Any existing notification will be cancelled.")
            jobScheduler.cancel(NOTIFICATION_JOB_ID)
        }

        val jobInfoBuilder = JobInfo.Builder(NOTIFICATION_JOB_ID, ComponentName(context, NotificationService::class.java))

        val absoluteNotificationTime = getNextNotificationTime()
        if (absoluteNotificationTime != -1L) {
            val relativeNotificationTimeFromNow = absoluteNotificationTime - Calendar.getInstance().timeInMillis
            // Notification should be shown within half an hour (before or after) the preferred time
            jobInfoBuilder.setMinimumLatency(relativeNotificationTimeFromNow - 30 * 60 * 1000)
            jobInfoBuilder.setOverrideDeadline(relativeNotificationTimeFromNow + 30 * 60 * 1000)

            Log.d(LOG_TAG, "Notification scheduled for around ${relativeNotificationTimeFromNow / 1000 / 60} minutes from now.")
        } else {
            // In this case, no notification should be shown
            Log.d(LOG_TAG, "No notification scheduled.")
            return -1
        }

        val bundle = PersistableBundle()
        bundle.putLong(KEY_NOTIFICATION_TIME, absoluteNotificationTime)
        jobInfoBuilder.setExtras(bundle)
        jobInfoBuilder.setPersisted(true)

        jobScheduler.schedule(jobInfoBuilder.build())
        /* Simplest way to make the class properly testable. */
        return absoluteNotificationTime
    }

    private fun getNextNotificationTime(): Long {
        /* Calculate the time the notification should be shown at */
        val notificationTime = Calendar.getInstance()

        val predefinedNotificationFrequencies = context.resources.getStringArray(R.array.pref_notifications_list_values)
        val preferredFrequency = sharedPreferences.getString("pref_notification_timing_list",
                predefinedNotificationFrequencies[0])
        val preferredTime = sharedPreferences.getInt("pref_notification_preferred_time", 1260)
        val preferredHour = preferredTime / 60
        val preferredMinute = preferredTime % 60

        val lastShownNotificationTimeInMillis: Long = repository.getLastNotificationShownAt()
        if (lastShownNotificationTimeInMillis == 0L) {   // No notification has been shown
            when (preferredFrequency) {
                predefinedNotificationFrequencies[0] ->
                    // Notification shouldn't be shown!
                    return -1
                predefinedNotificationFrequencies[1] ->
                    // Show a notification today, or if the preferred time has passed, tomorrow
                    if (preferredHour < notificationTime.get(Calendar.HOUR_OF_DAY))
                        notificationTime.add(Calendar.DAY_OF_MONTH, 1)
                predefinedNotificationFrequencies[2] ->
                    // Show a notification tomorrow
                    notificationTime.add(Calendar.DAY_OF_MONTH, 1)
                predefinedNotificationFrequencies[3] ->
                    // Show a notification 3 days from today
                    notificationTime.add(Calendar.DAY_OF_MONTH, 3)
                predefinedNotificationFrequencies[4] ->
                    // Show a notification 7 days from today
                    notificationTime.add(Calendar.DAY_OF_MONTH, 7)
            }
            // Set the hour and minute of the notification
            notificationTime.set(Calendar.HOUR_OF_DAY, preferredHour)
            notificationTime.set(Calendar.MINUTE, preferredMinute)
        } else {    // A notification has already been shown at a known time
            notificationTime.timeInMillis = lastShownNotificationTimeInMillis
            val currentTime = Calendar.getInstance()
            when (preferredFrequency) {
                predefinedNotificationFrequencies[0] ->
                    // Notification shouldn't be shown!
                    return -1
                predefinedNotificationFrequencies[1] ->
                    // Show a notification today, or if the preferred time has passed, tomorrow
                    if (preferredHour < notificationTime.get(Calendar.HOUR_OF_DAY))
                        notificationTime.add(Calendar.DAY_OF_MONTH, 1)
                predefinedNotificationFrequencies[2] ->
                    // Show a notification tomorrow
                    notificationTime.add(Calendar.DAY_OF_MONTH, 1)
                predefinedNotificationFrequencies[3] ->
                    // Show a notification 3 days from today
                    notificationTime.add(Calendar.DAY_OF_MONTH, 3)
                predefinedNotificationFrequencies[4] ->
                    // Show a notification 7 days from today
                    notificationTime.add(Calendar.DAY_OF_MONTH, 7)
            }
            notificationTime.set(Calendar.HOUR_OF_DAY, preferredHour)
            notificationTime.set(Calendar.MINUTE, preferredMinute)
            // If the scheduled date is in the past, set it to today
            if (notificationTime.before(currentTime)) {
                notificationTime.set(currentTime.get(Calendar.YEAR),
                        currentTime.get(Calendar.MONTH),
                        currentTime.get(Calendar.DAY_OF_MONTH))
            }
        }
        // If the preferred hour is past, set it to tomorrow
        if (preferredHour < notificationTime.get(Calendar.HOUR_OF_DAY))
            notificationTime.add(Calendar.DAY_OF_MONTH, 1)
        // If the notification would be shown within an hour, set it to tomorrow (no need to annoy the user)
        if (notificationTime.timeInMillis - Calendar.getInstance().timeInMillis <= 60 * 60 * 1000)
            notificationTime.add(Calendar.DAY_OF_MONTH, 1)

        return notificationTime.timeInMillis
    }
}
