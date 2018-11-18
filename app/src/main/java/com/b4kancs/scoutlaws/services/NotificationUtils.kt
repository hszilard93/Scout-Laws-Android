package com.b4kancs.scoutlaws.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log.DEBUG
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.quiz.QuizActivity
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment
import com.b4kancs.scoutlaws.views.quiz.picker.PickerFragment
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterFragment
import com.crashlytics.android.Crashlytics.log
import java.util.*

const val CHANNEL_ID = "NOTIFICATION_CHANNEL_1"
const val NOTIFICATION_ID = 1

/**
 * Created by hszilard on 16-Jul-18.
 */
private const val LOG_TAG = "NotificationUtils"

internal fun showQuizPromptNotification(context: Context) {
    log(DEBUG, LOG_TAG, "showQuizPromptNotification(..)")
    createNotificationChannel(context)
    val builder = makeQuizPromptNotificationBuilder(context)
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(NOTIFICATION_ID, builder.build())
}

private fun makeQuizPromptNotificationBuilder(context: Context): NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_lily_24dp)
                .setContentTitle(context.getString(R.string.quiz_notification_title))
                .setContentText(context.getString(R.string.quiz_notification_text))
                .setContentIntent(makeQuizIntent(context))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)

private fun makeQuizIntent(context: Context): PendingIntent {
    val quizIntent = Intent(context, QuizActivity::class.java)

    var type = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_notification_quiz_type", "0")
    if (type == "0")
        type = (Random().nextInt(3) + 1).toString()
    when (type) {
        "1" -> quizIntent.putExtra(QuizActivity.QUIZ_FRAGMENT_EXTRA, MultipleChoiceFragment.FRAGMENT_TAG)
        "2" -> quizIntent.putExtra(QuizActivity.QUIZ_FRAGMENT_EXTRA, PickerFragment.FRAGMENT_TAG)
        "3" -> quizIntent.putExtra(QuizActivity.QUIZ_FRAGMENT_EXTRA, SorterFragment.FRAGMENT_TAG)
        else -> throw IllegalStateException("Illegal quiz type: $type")
    }
    log(DEBUG, LOG_TAG, "Quiz type is: $type")

    val stackBuilder = TaskStackBuilder.create(context)
    stackBuilder.addNextIntentWithParentStack(quizIntent)
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)!!
}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel; only on API 26+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.resources.getString(R.string.quiz_notification_channel_name)
        val description = context.resources.getString(R.string.quiz_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}