package com.b4kancs.scoutlaws;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.b4kancs.scoutlaws.services.NotificationScheduler;
import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import androidx.annotation.VisibleForTesting;
import io.fabric.sdk.android.Fabric;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.LocaleUtilsKt.getBaseContextWithLocale;
import static com.b4kancs.scoutlaws.LocaleUtilsKt.refreshResources;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 05-Apr-18.
 */
public class ScoutLawApp extends Application {
    private static final String LOG_TAG = ScoutLawApp.class.getSimpleName();
    private static ScoutLawApp instance;

    @Inject protected NotificationScheduler notificationScheduler;
    private ApplicationComponent applicationComponent;

    public ScoutLawApp() {
        instance = this;
    }

    public static ScoutLawApp getInstance() {
        log(DEBUG, LOG_TAG, "getInstance()");
        if (instance == null)
            log(ERROR, LOG_TAG, "!!!App instance is null!!!");

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configureCrashReporting();
        log(INFO, LOG_TAG, "onCreate()");
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        // Reschedule notifications if necessary
        applicationComponent.inject(this);
        notificationScheduler.schedule(false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // Default language is Hungarian, can be changed at runtime (for demo purposes)
        super.attachBaseContext(getBaseContextWithLocale(base, "hu"));  // This will cause the default resource-set to be loaded
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        log(INFO, LOG_TAG, "Configuration changed.");
        super.onConfigurationChanged(newConfig);
        refreshResources(this);
    }

    private void configureCrashReporting() {
        Fabric.with(this, new Crashlytics());
    }

    @VisibleForTesting
    public void setApplicationComponent(ApplicationComponent appComponent) {
        this.applicationComponent = appComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
