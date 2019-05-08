package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.logger.Logger.log;

/**
 * Created by hszilard on 28-Feb-18.
 * This shared ViewModel contains quiz data relevant to more than one question.
 */

public class MultipleChoiceSharedViewModel extends AbstractSharedViewModel {
    private static final String LOG_TAG = MultipleChoiceSharedViewModel.class.getSimpleName();
    private final ArrayList<ScoutLaw> scoutLaws;

    public MultipleChoiceSharedViewModel() {
        scoutLaws = repository.getScoutLaws();
    }

    public List<ScoutLaw> getScoutLaws() {
        return scoutLaws;
    }

    @Override
    public long getBestTime() {
        return repository.getBestMultipleTime();
    }

    @Override
    protected void saveNewBestTime() {
        log(INFO, LOG_TAG, "saveNewBestTime(); time = " + timeSpent);
        repository.setBestMultipleTime(timeSpent);
    }
}
