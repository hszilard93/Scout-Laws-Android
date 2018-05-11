package com.b4kancs.scoutlaws.views.quiz.pickandchoose;

import com.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw;
import com.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hszilard on 08-Mar-18.
 */

public class PickAndChooseSharedViewModel extends AbstractSharedViewModel {
    private final ArrayList<PickAndChooseScoutLaw> pickChooseScoutLaws;

    public PickAndChooseSharedViewModel() {
        pickChooseScoutLaws = repository.getPickAndChooseLaws();
    }

    public List<PickAndChooseScoutLaw> getPickChooseScoutLaws() {
        return pickChooseScoutLaws;
    }
}
