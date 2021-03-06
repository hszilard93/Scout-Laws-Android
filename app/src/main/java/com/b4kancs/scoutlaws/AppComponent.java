package com.b4kancs.scoutlaws;

import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.services.NotificationService;
import com.b4kancs.scoutlaws.views.details.DetailsActivityViewModel;
import com.b4kancs.scoutlaws.views.menu.PrivacyDialogFragment;
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;
import com.b4kancs.scoutlaws.views.settings.PreferencesFragment;
import com.b4kancs.scoutlaws.views.settings.ResetInfoDialogFragment;
import com.b4kancs.scoutlaws.views.menu.AboutDialogFragment;
import com.b4kancs.scoutlaws.views.start.StartActivity;
import com.b4kancs.scoutlaws.views.start.StartActivityViewModel;
import com.b4kancs.scoutlaws.views.menu.StatsDialogFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hszilard on 05-Apr-18.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Repository repository);
    void inject(StartActivityViewModel startActivityViewModel);
    void inject(AbstractSharedViewModel abstractSharedViewModel);
    void inject(DetailsActivityViewModel detailsActivityViewModel);
    void inject(NotificationService notificationService);
    void inject(PreferencesFragment preferencesFragment);
    void inject(ResetInfoDialogFragment resetInfoDialogFragment);
    void inject(StatsDialogFragment statsDialogFragment);
    void inject(AboutDialogFragment aboutDialogFragment);
    void inject(PrivacyDialogFragment privacyDialogFragment);
    void inject(ExternalAvailableReceiver externalAvailableReceiver);
    void inject(App app);
    void inject(StartActivity startActivity);
}
