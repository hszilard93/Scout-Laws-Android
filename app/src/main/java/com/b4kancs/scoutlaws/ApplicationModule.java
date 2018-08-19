package com.b4kancs.scoutlaws;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.store.SharedPreferencesUserDataStore;
import com.b4kancs.scoutlaws.data.store.UserDataStore;
import com.b4kancs.scoutlaws.services.NotificationScheduler;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Module
public class ApplicationModule {
    private final static String USER_SHARED_PREFERENCES_KEY = "com.b4kancs.scoutlaws.user_shared_preferences";
    private final static String APP_SHARED_PREFERENCES_KEY = "com.b4kancs.scoutlaws.app_shared_preferences";

    private final Application application;

    ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    Resources provideResources() {
        return application.getResources();
    }

    @Provides
    @Named("user_preferences")
    SharedPreferences provideUserSharedPreferences() {
        return application.getSharedPreferences(USER_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    @Provides
    @Named("app_preferences")
    SharedPreferences provideApplicationSharedPreferences() {
        return application.getSharedPreferences(APP_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    @Provides
    UserDataStore provideUserDataStore(@Named("user_preferences") SharedPreferences userPreferences) {
        return new SharedPreferencesUserDataStore(userPreferences);
    }
}
