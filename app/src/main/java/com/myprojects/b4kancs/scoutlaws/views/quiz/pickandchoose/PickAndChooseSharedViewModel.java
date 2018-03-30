package com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose;

import com.myprojects.b4kancs.scoutlaws.data.Repository;
import com.myprojects.b4kancs.scoutlaws.data.model.PickAndChooseScoutLaw;
import com.myprojects.b4kancs.scoutlaws.views.quiz.AbstractSharedViewModel;

import java.util.ArrayList;

/**
 * Created by hszilard on 08-Mar-18.
 */

public class PickAndChooseSharedViewModel extends AbstractSharedViewModel {
    private final ArrayList<PickAndChooseScoutLaw> pickChooseScoutLaws;

    PickAndChooseSharedViewModel() {
        pickChooseScoutLaws = Repository.getInstance().getPickAndChooseLaws();
    }

    public ArrayList<PickAndChooseScoutLaw> getPickChooseScoutLaws() {
        return pickChooseScoutLaws;
    }
}
