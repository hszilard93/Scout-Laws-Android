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
import junit.framework.Assert.assertTrue
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
        `when`(mockContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)).thenReturn(mockJobScheduler)

        val mockResources: Resources = mock(Resources::class.java)
        `when`(mockResources.getStringArray(R.array.pref_notifications_list_values))
                .thenReturn(prefNotificationsListValues)
        `when`(mockContext.resources).thenReturn(mockResources)
    }

    @Test
    fun notificationSchedulerShouldCheckForExistingJobs() {
        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        notificationScheduler.schedule(false)

        verify(mockJobScheduler, times(1)).allPendingJobs
    }

    @Test
    fun whenForcedIsTrueNotificationSchedulerShouldCancelAllExistingNotifications() {
        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        notificationScheduler.schedule(false)

        notificationScheduler.schedule(true)

        verify(mockJobScheduler, times(1)).cancel(1)
    }

    @Test
    fun whenForcedIsFalseAndNotificationIsScheduledFarEnoughItShouldNotBeRescheduled() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance()
        // Notification time must be more than 60 minutes in the future to not be rescheduled
        notificationTime.add(Calendar.MINUTE, 61)

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        notificationScheduler.schedule(false)

        verify(mockJobScheduler, never()).schedule(any())
    }

    @Test
    fun whenForcedIsFalseAndNotificationIsNotScheduledFarEnoughItShouldBeRescheduled() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance()
        // Notification time must be more than 60 minutes in the future to not be rescheduled
        notificationTime.add(Calendar.MINUTE, 60)

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        notificationScheduler.schedule(false)

        verify(mockJobScheduler, times(1)).schedule(any())
    }

    @Test
    fun whenFrequencyIsNotNeverNotificationSchedulerShouldRescheduleNotification() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance()
        // Notification time must be more than 60 minutes in the future to not be automatically rescheduled
        notificationTime.add(Calendar.MINUTE, 61)

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))
        `when`(mockJobScheduler.schedule(any(JobInfo::class.java))).then { Log.d("Something", "something") }
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        notificationScheduler.schedule(true)

        verify(mockJobScheduler, times(1)).schedule(any())
    }

    @Test
    fun whenFrequencyIsNeverNotificationSchedulerShouldNotRescheduleNotification() {
        val mockJobInfo = mock(JobInfo::class.java)
        val mockBundle = mock(PersistableBundle::class.java)
        val notificationTime = Calendar.getInstance()
        // Notification time must be more than 60 minutes in the future to not be automatically rescheduled
        notificationTime.add(Calendar.MINUTE, 61)

        `when`(mockBundle.getLong("KEY_NOTIFICATION_TIME")).thenReturn(notificationTime.timeInMillis)
        `when`(mockJobInfo.id).thenReturn(1)
        `when`(mockJobInfo.extras).thenReturn(mockBundle)
        `when`(mockJobScheduler.allPendingJobs).thenReturn(listOf(mockJobInfo))
        `when`(mockJobScheduler.schedule(any(JobInfo::class.java))).then { Log.d("Something", "something") }
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[0])

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        notificationScheduler.schedule(true)

        verify(mockJobScheduler, times(0)).schedule(any())
    }

    @Test
    fun whenForcedIsTrueAndPreferredFrequencyIsOnceADayAndPreferredTimeIsCloseSchedulerShouldScheduleForNextDay() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return 1500
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 100)

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        val notificationTimeInMillis = notificationScheduler.schedule(false)

        val notificationTimeInCalendar = Calendar.getInstance()
        notificationTimeInCalendar.timeInMillis = notificationTimeInMillis
        val tomorrowInCalendar = Calendar.getInstance()
        tomorrowInCalendar.add(Calendar.DATE, 1)

        assertEquals(tomorrowInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsOnceADayAndPreferredTimeIsFarSchedulerShouldScheduleForToday() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return 1601
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1) * 100 + 1)

        /* Test will always fail after 2200. Pass automatically. */
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 22) return

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        val notificationTimeInMillis = notificationScheduler.schedule(false)
        val notificationTimeInCalendar = Calendar.getInstance()
        notificationTimeInCalendar.timeInMillis = notificationTimeInMillis

        assertEquals(Calendar.getInstance().get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsTwoDaysSchedulerShouldScheduleForTomorrow() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[2])

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        val notificationTimeInMillis = notificationScheduler.schedule(false)
        val notificationTimeInCalendar = Calendar.getInstance()
        notificationTimeInCalendar.timeInMillis = notificationTimeInMillis
        val tomorrowInCalendar = Calendar.getInstance()
        tomorrowInCalendar.add(Calendar.DATE, 1)

        assertEquals(tomorrowInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenAndPreferredFrequencyIsTwiceAWeekSchedulerShouldScheduleForThreeDaysFromNow() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[3])

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        val notificationTimeInMillis = notificationScheduler.schedule(false)
        val notificationTimeInCalendar = Calendar.getInstance()
        notificationTimeInCalendar.timeInMillis = notificationTimeInMillis
        val threeDaysInCalendar = Calendar.getInstance()
        threeDaysInCalendar.add(Calendar.DATE, 3)

        assertEquals(threeDaysInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun whenPreferredFrequencyIsOnceAWeekSchedulerShouldScheduleForSevenDaysFromNow() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[4])

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        val notificationTimeInMillis = notificationScheduler.schedule(false)
        val notificationTimeInCalendar = Calendar.getInstance()
        notificationTimeInCalendar.timeInMillis = notificationTimeInMillis
        val oneWeekInCalendar = Calendar.getInstance()
        oneWeekInCalendar.add(Calendar.DATE, 7)

        assertEquals(oneWeekInCalendar.get(Calendar.DATE), notificationTimeInCalendar.get(Calendar.DATE))
    }

    @Test
    fun newNotificationShouldBeScheduledForPreferredTime() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn(2135)

        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)
        val notificationTimeInMillis = notificationScheduler.schedule(false)
        val notificationTimeInCalendar = Calendar.getInstance()
        notificationTimeInCalendar.timeInMillis = notificationTimeInMillis

        assertEquals(21, notificationTimeInCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, notificationTimeInCalendar.get(Calendar.MINUTE))
    }
}