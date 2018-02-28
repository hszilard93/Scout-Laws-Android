package com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myprojects.b4kancs.scoutlaws.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickAndChooseFragment extends Fragment {


    public PickAndChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick, container, false);
    }

}
