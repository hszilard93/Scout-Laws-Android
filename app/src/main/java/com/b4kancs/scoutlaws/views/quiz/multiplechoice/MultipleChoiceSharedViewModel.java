package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;

import java.util.ArrayList;

/**
 * Created by hszilard on 28-Feb-18.
 * This shared ViewModel contains quiz data relevant to more than one question.
 */

public class MultipleChoiceSharedViewModel extends AbstractSharedViewModel {
    private final ArrayList<ScoutLaw> scoutLaws;

    public MultipleChoiceSharedViewModel() {
        scoutLaws = repository.getLaws();
    }

    public ArrayList<ScoutLaw> getScoutLaws() {
        return scoutLaws;
    }
}
