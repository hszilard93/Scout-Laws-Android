package com.b4kancs.scoutlaws.views.details;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import javax.inject.Inject;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 21-Feb-18.
 */
public class DetailsActivityViewModel extends ViewModel {
    private static final String LOG_TAG = DetailsActivityViewModel.class.getSimpleName();

    public enum State {MODERN, OLD}

    @Inject protected Repository repository;
    /* This flag helps us retain state after config changes */
    private final ObservableField<State> observableState = new ObservableField<>();
    private final int index;
    private ScoutLaw scoutLaw;

    /* Scout law numbers start from 1!*/
    DetailsActivityViewModel(int scoutLawNumber) {
        this.index = scoutLawNumber;
        init();
    }

    private void init() {
        log(DEBUG, LOG_TAG, "init()");
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        this.scoutLaw = repository.getScoutLaws().get(index - 1);
        observableState.set(State.MODERN);
    }

    public ScoutLaw scoutLaw() {
        return scoutLaw;
    }

    public ObservableField<State> observableState() {
        return observableState;
    }

    public void setState(State state) {
        log(INFO, LOG_TAG, "setState(); news state: " + state);
        observableState.set(state);
    }
}
