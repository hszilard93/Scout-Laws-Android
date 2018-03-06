package com.myprojects.b4kancs.scoutlaws.views.quiz.chooser;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.FragmentChooserBinding;
import com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModel;
import com.myprojects.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseFragment;

import java.util.Random;

import static com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment.getMultipleChoiceFragmentTransaction;

/**
 * Created by hszilard on 25-Feb-18.
 * Choose a quiz type.
 */
public class ChooserFragment extends Fragment {
    private static final String LOG_TAG = ChooserFragment.class.getSimpleName();

    private FragmentChooserBinding binding;
    private ViewGroup container;

    public ChooserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chooser, container, false);
        setUpViews();

        setRetainInstance(true);
        return binding.getRoot();
    }

    private void setUpViews() {
        binding.chooserAnyButton.setOnClickListener(click -> {
            Log.d(LOG_TAG, "Any quiz button pressed.");
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
                startMultipleChoiceFragment();
            else
                startPickAndChooseFragment();
        });
        binding.chooserMultipleButton.setOnClickListener(click -> {
            Log.d(LOG_TAG, "Multiple choice button pressed.");
            startMultipleChoiceFragment();
        });
        binding.chooserPickButton.setOnClickListener(click -> {
            Log.d(LOG_TAG, "Pick and choose button pressed.");
            startPickAndChooseFragment();
        });
    }

    private void startMultipleChoiceFragment() {
        MultipleChoiceSharedViewModel multipleSharedViewModel = ViewModelProviders.of(getActivity())
                .get(MultipleChoiceSharedViewModel.class);
        multipleSharedViewModel.reset();
        FragmentTransaction transaction = getMultipleChoiceFragmentTransaction(container, getFragmentManager());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startPickAndChooseFragment() {
        PickAndChooseFragment fragment = new PickAndChooseFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(container.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
