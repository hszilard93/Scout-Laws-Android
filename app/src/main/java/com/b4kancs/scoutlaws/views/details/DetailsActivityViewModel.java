package com.b4kancs.scoutlaws.views.details;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.util.Log;

import com.b4kancs.scoutlaws.ScoutLawApp;
import com.b4kancs.scoutlaws.data.Repository;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;

import javax.inject.Inject;

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
        Log.d(LOG_TAG, "Initiating.");
        ScoutLawApp.getInstance().getApplicationComponent().inject(this);
        this.scoutLaw = repository.getLaws().get(index - 1);
        observableState.set(State.MODERN);
    }

    public ScoutLaw scoutLaw() {
        return scoutLaw;
    }

    public ObservableField<State> observableState() {
        return observableState;
    }

    public void setState(State state) {
        Log.d(LOG_TAG, "Setting observableState to: " + state);
        observableState.set(state);
    }
}
