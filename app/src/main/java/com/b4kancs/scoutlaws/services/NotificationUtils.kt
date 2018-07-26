package com.b4kancs.scoutlaws.services

import android.content.Context
import android.support.v4.app.NotificationCompat
import com.b4kancs.scoutlaws.R
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.support.v4.app.NotificationManagerCompat

const val CHANNEL_ID = "NOTIFICATION_CHANNEL_1"
const val NOTIFICATION_ID = 1

/**
 * Created by hszilard on 16-Jul-18.
 */
fun showQuizPromptNotification(context: Context) {
    createNotificationChannel(context)
    val builder = raiseQuizPromptNotification(context)
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(NOTIFICATION_ID, builder.build())
}

private fun raiseQuizPromptNotification(context: Context): NotificationCompat.Builder =
    NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_lily_24dp)
            .setContentTitle(context.getString(R.string.quiz_notification_title))
            .setContentText(context.getString(R.string.quiz_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.resources.getString(R.string.quiz_notification_channel_name)
        val description = context.resources.getString(R.string.quiz_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}