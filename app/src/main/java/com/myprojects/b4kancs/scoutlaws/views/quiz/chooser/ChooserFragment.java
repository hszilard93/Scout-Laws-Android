package com.myprojects.b4kancs.scoutlaws.views.quiz.chooser;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.FragmentChooserBinding;
import com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment;
import com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseFragment;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooserFragment extends Fragment {
    private static final String LOG_TAG = ChooserFragment.class.getSimpleName();

    private FragmentChooserBinding binding;
    private ViewGroup container;

    public ChooserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chooser, container, false);
        setUpViews();

        setRetainInstance(true);
        return binding.getRoot();
    }

    private void setUpViews() {
        binding.chooserAnyButton.setOnClickListener(click -> {
            Log.d(LOG_TAG, "Any button button pressed.");
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
                startMultipleChoiceFragment();
            else
                startPickAndChooseFragment();
        });
        binding.chooserMultipleButton.setOnClickListener(click -> {
            Log.d(LOG_TAG, "Multiple choice button button pressed.");
            startMultipleChoiceFragment();
        });
        binding.chooserPickButton.setOnClickListener(click -> {
            Log.d(LOG_TAG, "Pick and choose button button pressed.");
            startPickAndChooseFragment();
        });
    }

    private void startMultipleChoiceFragment() {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(container.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startPickAndChooseFragment() {
        PickAndChooseFragment fragment = new PickAndChooseFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(container.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
