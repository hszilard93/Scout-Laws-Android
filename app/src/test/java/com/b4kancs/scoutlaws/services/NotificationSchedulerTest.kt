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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Inject

/**
 * Created by hszilard on 25-Aug-18.
 */

private val prefNotificationsListValues = arrayOf("never", "one day", "two days", "twice a week", "once a week")

class NotificationSchedulerTest {

    @Inject lateinit var repository: Repository
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockSharedPreferences: SharedPreferences
    @Mock private lateinit var mockJobScheduler: JobScheduler

    init {
        /* Set up Dagger for testing */
        val testComponent: TestComponent = DaggerTestComponent.builder().testModule((TestModule())).build()
        testComponent.inject(this)
        ScoutLawApp().applicationComponent = testComponent
        /* Set up Mockito mocks */
        MockitoAnnotations.initMocks(this)
    }

    @BeforeEach
    fun setUpEach() {
        mock(Resources::class.java).apply {
            `when`(getStringArray(R.array.pref_notifications_list_values))
                    .thenReturn(prefNotificationsListValues)
            `when`(mockContext.resources).thenReturn(this)
        }
        `when`(mockContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)).thenReturn(mockJobScheduler)
    }

    @Test
    fun notificationSchedulerShouldCheckForExistingJobs() {
        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)

        verify(mockJobScheduler, times(1)).allPendingJobs
    }

    @Test
    fun whenForcedIsTrueNotificationSchedulerShouldCancelAllExistingNotifications() {
        NotificationScheduler(mockContext, repository, mockSharedPreferences).apply {
            schedule(false)
            schedule(true)
        }

        verify(mockJobScheduler, times(1)).cancel(1)
    }

    @Test
    fun whenForcedIsFalseAndNotificationIsScheduledFarEnoughItShouldNotBeRescheduled() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 61) // Notification time must be more than 60 minutes in the future to not be rescheduled
        }

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)

        verify(mockJobScheduler, never()).schedule(any())
    }

    @Test
    fun whenForcedIsFalseAndNotificationIsNotScheduledFarEnoughItShouldBeRescheduled() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 60) // Notification time must be more than 60 minutes in the future to not be rescheduled
        }

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)

        verify(mockJobScheduler, times(1)).schedule(any())
    }

    @Test
    fun whenFrequencyIsNotNeverNotificationSchedulerShouldRescheduleNotification() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 61) // Notification time must be more than 60 minutes in the future to not be automatically rescheduled
        }

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))
        `when`(mockJobScheduler.schedule(any(JobInfo::class.java))).then { Log.d("Something", "something") }
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(true)

        verify(mockJobScheduler, times(1)).schedule(any())
    }

    @Test
    fun whenFrequencyIsNeverNotificationSchedulerShouldNotRescheduleNotification() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 61) // Notification time must be more than 60 minutes in the future to not be automatically rescheduled
        }

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))
        `when`(mockJobScheduler.schedule(any(JobInfo::class.java))).then { Log.d("Something", "something") }
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[0])

        NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(true)

        verify(mockJobScheduler, times(0)).schedule(any())
    }

    @Test
    fun whenForcedIsTrueAndPreferredFrequencyIsOnceADayAndPreferredTimeIsCloseSchedulerShouldScheduleForNextDay() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return 1600
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1) * 100)

        val notificationTimeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences)
                .schedule(false)
        val notificationTimeInCalendar = Calendar.getInstance().apply { timeInMillis = notificationTimeInMillis }
        val tomorrowInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 1) }

        assertEquals(tomorrowInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsOnceADayAndPreferredTimeIsFarSchedulerShouldScheduleForToday() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return (15+2) * 60 = 1020
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2) * 60)

        /* Test will always fail after 2200. Pass automatically. */
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 22) return

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }

        assertEquals(Calendar.getInstance().get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsTwoDaysSchedulerShouldScheduleForTomorrow() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[2])

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }
        val tomorrowInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 1) }

        assertEquals(tomorrowInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenAndPreferredFrequencyIsTwiceAWeekSchedulerShouldScheduleForThreeDaysFromNow() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[3])

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }
        val threeDaysInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 3) }

        assertEquals(threeDaysInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsOnceAWeekSchedulerShouldScheduleForSevenDaysFromNow() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[4])

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }
        val oneWeekInCalendar = Calendar.getInstance().apply { add(Calendar.DATE, 7) }

        assertEquals(oneWeekInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun newNotificationShouldBeScheduledForPreferredTime() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn(1295)   // 21:35

        val notificationTimeInCalendar = Calendar.getInstance().apply {
            timeInMillis = NotificationScheduler(mockContext, repository, mockSharedPreferences).schedule(false)
        }

        assertEquals(21, notificationTimeInCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, notificationTimeInCalendar.get(Calendar.MINUTE))
    }
}