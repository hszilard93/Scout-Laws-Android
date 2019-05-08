package com.b4kancs.scoutlaws;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.VisibleForTesting;
import androidx.preference.PreferenceManager;

import com.b4kancs.scoutlaws.logger.CrashlyticsLogger;
import com.b4kancs.scoutlaws.logger.DefaultLogger;
import com.b4kancs.scoutlaws.logger.Logger;
import com.b4kancs.scoutlaws.logger.LoggerI;
import com.b4kancs.scoutlaws.services.NotificationScheduler;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.LocaleUtilsKt.getBaseContextWithLocale;
import static com.b4kancs.scoutlaws.LocaleUtilsKt.refreshResources;

/**
 * Created by hszilard on 05-Apr-18.
 */
public class App extends Application {
    private static final String LOG_TAG = App.class.getSimpleName();
    private static App instance;

    @Inject protected NotificationScheduler notificationScheduler;
    private AppComponent appComponent;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        Logger.Companion.log(DEBUG, LOG_TAG, "getInstance()");
        if (instance == null)
            Logger.Companion.log(ERROR, LOG_TAG, "!!!App instance is null!!!");

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configureCrashReporting();
        Logger.Companion.log(INFO, LOG_TAG, "onCreate()");
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
        // Reschedule notifications if necessary
        notificationScheduler.schedule(false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // Default language is Hungarian, can be changed at runtime (for demo purposes)
        super.attachBaseContext(getBaseContextWithLocale(base, "hu"));  // This will cause the default resource-set to be loaded
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.Companion.log(INFO, LOG_TAG, "Configuration changed.");
        super.onConfigurationChanged(newConfig);
        refreshResources(this);
    }

    private void configureCrashReporting() {
        LoggerI logger;
        boolean isCrashReportingEnabled = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("pref_crash_reports", true);
        if (isCrashReportingEnabled) {
            logger = new CrashlyticsLogger();
            FirebaseAnalytics.getInstance(getApplicationContext()).setAnalyticsCollectionEnabled(true);
        } else {
            logger = new DefaultLogger();
            FirebaseAnalytics.getInstance(getApplicationContext()).setAnalyticsCollectionEnabled(false);
        }

        Logger.Companion.setLogger(logger);
    }

    @VisibleForTesting
    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
