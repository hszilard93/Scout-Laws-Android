package com.b4kancs.scoutlaws;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.b4kancs.scoutlaws.data.store.SharedPreferencesUserDataStore;
import com.b4kancs.scoutlaws.data.store.UserDataStore;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.logger.Logger.log;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Module
public class AppModule {
    private final static String LOG_TAG = AppModule.class.getSimpleName();
    private final static String USER_SHARED_PREFERENCES_KEY = "com.b4kancs.scoutlaws.user_shared_preferences";
    private final static String RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY = "RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY";

    private final Application application;

    AppModule(Application application) {
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
    @Named("default_preferences")
    SharedPreferences provideDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    UserDataStore provideUserDataStore(@Named("user_preferences") SharedPreferences userPreferences) {
        return new SharedPreferencesUserDataStore(userPreferences);
    }

    @Provides
    @Singleton
    @Named("release_notes")
    boolean provideShouldShowReleaseNotes(Context applicationContext, @Named("default_preferences") SharedPreferences preferences) {
        log(DEBUG, LOG_TAG, "provideShouldShowReleaseNotes(); Checking version code..");
        int versionCode = BuildConfig.VERSION_CODE;
        if (preferences.getInt(RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY, 0) < versionCode) {
            log(INFO, LOG_TAG, "Release notes not shown. Build version is " + versionCode);
            preferences.edit().putInt(RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY, versionCode).apply();
            return true;
        } else
            return false;
    }
}
