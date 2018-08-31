package com.b4kancs.scoutlaws

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.b4kancs.scoutlaws.data.store.UserDataStore
import com.b4kancs.scoutlaws.services.NotificationScheduler
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hszilard on 08-May-18.
 */
@Module
class TestModule {

    companion object {
        const val NUMBER_OF_LAWS = 5
    }

    @Provides
    fun provideResources(): Resources = getStubResourcesWithFiveLaws()

    @Provides
    fun provideApplicationContext(): Context = mock(Context::class.java)

    @Provides
    fun provideUserDataStore(): UserDataStore = StubUserDataStore()

    @Provides
    @Named("default_preferences")
    fun provideSharedPreferences(): SharedPreferences = mock(SharedPreferences::class.java)

    @Provides
    fun provideNotificationScheduler(): NotificationScheduler = mock(NotificationScheduler::class.java)

}