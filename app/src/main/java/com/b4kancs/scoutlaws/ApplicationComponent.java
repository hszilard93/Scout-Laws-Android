package com.b4kancs.scoutlaws;

import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.services.NotificationService;
import com.b4kancs.scoutlaws.views.details.DetailsActivityViewModel;
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;
import com.b4kancs.scoutlaws.views.settings.PreferencesFragment;
import com.b4kancs.scoutlaws.views.settings.ResetInfoDialogFragment;
import com.b4kancs.scoutlaws.views.start.AboutDialogFragment;
import com.b4kancs.scoutlaws.views.start.StartActivityViewModel;
import com.b4kancs.scoutlaws.views.start.StatsDialogFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(Repository repository);
    void inject(StartActivityViewModel startActivityViewModel);
    void inject(AbstractSharedViewModel abstractSharedViewModel);
    void inject(DetailsActivityViewModel detailsActivityViewModel);
    void inject(NotificationService notificationService);
    void inject(PreferencesFragment preferencesFragment);
    void inject(ResetInfoDialogFragment resetInfoDialogFragment);
    void inject(AboutDialogFragment aboutDialogFragment);
    void inject(StatsDialogFragment statsDialogFragment);
    void inject(ExternalAvailableReceiver externalAvailableReceiver);
    void inject(ScoutLawApp scoutLawApp);
}
