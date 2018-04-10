package com.myprojects.b4kancs.scoutlaws;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.views.details.DetailsActivityViewModel;
import com.myprojects.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;
import com.myprojects.b4kancs.scoutlaws.views.start.StartActivityViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(Repository repository);
    void inject(ScoutLawApp app);
    void inject(StartActivityViewModel startActivityViewModel);
    void inject(AbstractSharedViewModel abstractSharedViewModel);
    void inject(DetailsActivityViewModel detailsActivityViewModel);
}
