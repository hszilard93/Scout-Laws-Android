package com.b4kancs.scoutlaws.views.quiz.picker;

import com.b4kancs.scoutlaws.data.model.PickerScoutLaw;
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.INFO;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 08-Mar-18.
 */
public class PickerSharedViewModel extends AbstractSharedViewModel {
    private final static String LOG_TAG = PickerSharedViewModel.class.getSimpleName();
    private final ArrayList<PickerScoutLaw> pickerScoutLaws;

    public PickerSharedViewModel() {
        pickerScoutLaws = repository.getPickerScoutLaws();
    }

    public List<PickerScoutLaw> getPickerScoutLaws() {
        return pickerScoutLaws;
    }

    @Override
    public long getBestTime() {
        return repository.getBestPickerTime();
    }

    @Override
    protected void saveNewBestTime() {
        log(INFO, LOG_TAG, "saveNewBestTime(); time = " + timeSpent);
        repository.setBestPickerTime(timeSpent);
    }
}
