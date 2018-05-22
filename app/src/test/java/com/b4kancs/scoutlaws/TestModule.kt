package com.b4kancs.scoutlaws

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.b4kancs.scoutlaws.data.store.UserDataStore
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

/**
 * Created by hszilard on 08-May-18.
 */
@Module
class TestModule {

    companion object {
        const val NUMBER_OF_LAWS = 5
    }

    @Provides
    fun provideResources(): Resources {
        return getStubResourcesWithFiveLaws()
    }

    @Provides
    fun provideApplicationContext(): Context {
        return mock(Context::class.java)
    }

    @Provides
    fun provideUserDataStore(): UserDataStore {
        return StubUserDataStore()
    }
}