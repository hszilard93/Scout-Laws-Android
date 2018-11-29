package com.b4kancs.scoutlaws;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.b4kancs.scoutlaws.data.store.SharedPreferencesUserDataStore;
import com.b4kancs.scoutlaws.data.store.UserDataStore;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Module
public class ApplicationModule {
    private final static String LOG_TAG = ApplicationModule.class.getSimpleName();
    private final static String USER_SHARED_PREFERENCES_KEY = "com.b4kancs.scoutlaws.user_shared_preferences";
    private final static String RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY = "RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY";

    private static boolean isReleaseChecked = false;

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
    @Named("default_preferences")
    SharedPreferences provideDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    UserDataStore provideUserDataStore(@Named("user_preferences") SharedPreferences userPreferences) {
        return new SharedPreferencesUserDataStore(userPreferences);
    }

    @Provides
    @Named("release_notes")
    boolean shouldShowReleaseNotes(Context applicationContext, @Named("default_preferences") SharedPreferences preferences) {
        if (!isReleaseChecked) {    // Do this only once per application session!
            isReleaseChecked = true;
            log(DEBUG, LOG_TAG, "shouldShowReleaseNotes(); Checking version code..");
            int versionCode = BuildConfig.VERSION_CODE;
            if (preferences.getInt(RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY, 0) < versionCode) {
                log(INFO, LOG_TAG, "Release notes not shown. Build version is " + versionCode);
                preferences.edit().putInt(RELEASE_NOTES_SHOWN_FOR_VERSION_CODE_KEY, versionCode).apply();
                return true;
            } else
                return false;
        }
        else
            return false;
    }
}
