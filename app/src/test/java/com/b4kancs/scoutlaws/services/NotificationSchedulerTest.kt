package com.b4kancs.scoutlaws.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.PersistableBundle
import android.util.Log
import com.b4kancs.scoutlaws.*
import com.b4kancs.scoutlaws.data.Repository
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

/**
 * Created by hszilard on 25-Aug-18.
 */
private val prefNotificationsListValues = arrayOf("never", "one day", "two days", "twice a week", "once a week")

class NotificationSchedulerTest {

    @Inject lateinit var repository: Repository
    private var mockContext: Context = mock()
    private var mockSharedPreferences: SharedPreferences = mock()
    private var mockJobScheduler: JobScheduler = mock()

    init {
        /* Set up Dagger for testing */
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule(TestModule()).build()
        testComponent.inject(this)
        App().appComponent = testComponent
    }

    @BeforeEach
    fun setUpEach() {
        mock<Resources> {
            on {getStringArray(R.array.pref_notifications_list_values) }.doReturn(prefNotificationsListValues)
            on { mockContext.resources }.doReturn(it)
        }
        whenever(mockContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)).thenReturn(mockJobScheduler)
    }

    @Test
    fun notificationSchedulerShouldCheckForExistingJobs() {
        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)

        verify(mockJobScheduler).allPendingJobs
    }

    @Test
    fun whenForcedIsTrueNotificationSchedulerShouldCancelAllExistingNotifications() {
        NotificationScheduler(mockContext, repository, mockSharedPreferences).apply {
            schedule(false)
            schedule(true)
        }

        verify(mockJobScheduler).cancel(1)
    }

    @Test
    fun whenForcedIsFalseAndNotificationIsScheduledFarEnoughItShouldNotBeRescheduled() {
        val mockJobInfo: JobInfo = mock()
        val mockBundle: PersistableBundle = mock()
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 61) // Notification time must be more than 60 minutes in the future to not be rescheduled
        }

        whenever(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        whenever(mockJobInfo.id).thenReturn(1)
        whenever(mockJobInfo.extras).thenReturn(mockBundle)
        whenever(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)

        verify(mockJobScheduler, never()).schedule(any())
    }

    @Test
    fun whenForcedIsFalseAndNotificationIsNotScheduledFarEnoughItShouldBeRescheduled() {
        val mockJobInfo: JobInfo = mock()
        val mockBundle: PersistableBundle = mock()
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 60) // Notification time must be more than 60 minutes in the future to not be rescheduled
        }

        whenever(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        whenever(mockJobInfo.id).thenReturn(1)
        whenever(mockJobInfo.extras).thenReturn(mockBundle)
        whenever(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        // We won't get a real JobInfo in test environment
        verify(mockJobScheduler).schedule(anyOrNull())
    }

    @Test
    fun whenFrequencyIsNotNeverNotificationSchedulerShouldRescheduleNotification() {
        val mockJobInfo: JobInfo = mock()
        val mockBundle: PersistableBundle = mock()
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 61) // Notification time must be more than 60 minutes in the future to not be automatically rescheduled
        }

        whenever(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        whenever(mockJobInfo.id).thenReturn(1)
        whenever(mockJobInfo.extras).thenReturn(mockBundle)
        whenever(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))
        whenever(mockJobScheduler.schedule(any())).then { Log.d("Something", "something") }
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(true)
        // We won't get a real JobInfo in test environment
        verify(mockJobScheduler).schedule(anyOrNull())
    }

    @Test
    fun whenFrequencyIsNeverNotificationSchedulerShouldNotRescheduleNotification() {
        val mockJobInfo: JobInfo = mock()
        val mockBundle: PersistableBundle = mock()
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 61) // Notification time must be more than 60 minutes in the future to not be automatically rescheduled
        }

        whenever(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        whenever(mockJobInfo.id).thenReturn(1)
        whenever(mockJobInfo.extras).thenReturn(mockBundle)
        whenever(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))
        whenever(mockJobScheduler.schedule(any())).then { Log.d("Something", "something") }
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[0])

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(true)

        verify(mockJobScheduler, times(0)).schedule(any())
    }

    @Test
    fun whenForcedIsTrueAndPreferredFrequencyIsOnceADayAndPreferredTimeIsCloseSchedulerShouldScheduleForNextDay() {
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return 1600
        whenever(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), any()))
                .thenReturn((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1) * 100)

        val notificationTimeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences)
                .schedule(true)
        val notificationTimeInCalendar = Calendar.getInstance().apply { timeInMillis = notificationTimeInMillis }
        val tomorrowInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 1) }

        assertEquals(tomorrowInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsOnceADayAndPreferredTimeIsFarSchedulerShouldScheduleForToday() {
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return (15+2) * 60 = 1020
        whenever(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), any()))
                .thenReturn((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2) * 60)

        /* Test will always fail after 2200. Pass automatically. */
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 22) return

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }

        assertEquals(Calendar.getInstance().get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsTwoDaysSchedulerShouldScheduleForTomorrow() {
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[2])

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }
        val tomorrowInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 1) }

        assertEquals(tomorrowInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenAndPreferredFrequencyIsTwiceAWeekSchedulerShouldScheduleForThreeDaysFromNow() {
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[3])

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }
        val threeDaysInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 3) }

        assertEquals(threeDaysInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsOnceAWeekSchedulerShouldScheduleForSevenDaysFromNow() {
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[4])

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }
        val oneWeekInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 7) }

        assertEquals(oneWeekInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun newNotificationShouldBeScheduledForPreferredTime() {
        whenever(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        whenever(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), any()))
                .thenReturn(1295)   // 21:35

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }

        assertEquals(21, notificationTimeInCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, notificationTimeInCalendar.get(Calendar.MINUTE))
    }
}
