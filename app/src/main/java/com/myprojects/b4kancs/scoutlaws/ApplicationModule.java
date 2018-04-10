package com.myprojects.b4kancs.scoutlaws;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Module
public class ApplicationModule {

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
}
