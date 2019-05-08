package com.b4kancs.scoutlaws

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.b4kancs.scoutlaws.data.store.UserDataStore
import com.b4kancs.scoutlaws.services.NotificationScheduler
import com.b4kancs.scoutlaws.logger.DefaultLogger
import com.b4kancs.scoutlaws.logger.LoggerI
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import javax.inject.Named

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
    fun provideApplicationContext(): Context = mock()

    @Provides
    fun provideUserDataStore(): UserDataStore = StubUserDataStore()

    @Provides
    @Named("default_preferences")
    fun provideSharedPreferences(): SharedPreferences = mock()

    @Provides
    fun provideNotificationScheduler(): NotificationScheduler = mock()

    @Provides
    @Named("release_notes")
    fun shouldShowReleaseNotes(): Boolean = false
}
