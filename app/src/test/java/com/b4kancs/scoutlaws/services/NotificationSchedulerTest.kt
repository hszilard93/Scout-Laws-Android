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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
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
    fun notificationSchedulerShouldAutomaticallyCheckForExistingJobsAfterInitialization() {
        NotificationScheduler(mockContext, repository, mockSharedPreferences)

        verify(mockJobScheduler, times(1)).allPendingJobs
    }

    @Test
    fun whenForcedIsTrueNotificationSchedulerShouldCancelAllExistingNotifications() {
        val notificationScheduler = NotificationScheduler(mockContext, repository, mockSharedPreferences)

        notificationScheduler.scheduleNotification(true)

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

        NotificationScheduler(mockContext, repository, mockSharedPreferences)

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

        NotificationScheduler(mockContext, repository, mockSharedPreferences)

        verify(mockJobScheduler, times(1)).schedule(any())
    }

    @Test
    fun whenForcedIsTrueAndFrequencyIsNotNeverNotificationSchedulerShouldRescheduleNotification() {
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
        notificationScheduler.scheduleNotification(true)

        verify(mockJobScheduler, times(1)).schedule(any())
    }

    @Test
    fun whenForcedIsTrueAndPreferredFrequencyIsOnceADayAndPreferredTimeIsCloseSchedulerShouldScheduleForNextDay() {
        `when`(mockSharedPreferences.getString(eq("pref_notification_timing_list"), any()))
                .thenReturn(prefNotificationsListValues[1])
        // If current hour is 15, return 1500
        `when`(mockSharedPreferences.getInt(eq("pref_notification_preferred_time"), anyInt()))
                .thenReturn(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 100)

        NotificationScheduler(mockContext, repository, mockSharedPreferences)

        val captor = ArgumentCaptor.forClass(JobInfo::class.java)
        verify(mockJobScheduler).schedule(captor.capture())
        assertTrue(captor.value != null)
    }
}