package com.myprojects.b4kancs.scoutlaws;

import android.content.Context;

import com.myprojects.b4kancs.scoutlaws.data.Repository;

import dagger.Component;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(Repository repository);

    void inject(ScoutLawApp app);

    Context getContext();
}
