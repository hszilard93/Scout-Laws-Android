package com.myprojects.b4kancs.scoutlaws;

import android.app.Application;
import android.util.Log;

/**
 * Created by hszilard on 05-Apr-18.
 */
public class ScoutLawApp extends Application {
    private static final String LOG_TAG = ScoutLawApp.class.getSimpleName();
    private static ScoutLawApp instance;

    protected ApplicationComponent applicationComponent;

    protected ScoutLawApp() {
        instance = this;
    }

    public static ScoutLawApp getInstance() {
        if (instance != null)
            return instance;
        else {
            Log.e(LOG_TAG, "App instance is null!");
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
