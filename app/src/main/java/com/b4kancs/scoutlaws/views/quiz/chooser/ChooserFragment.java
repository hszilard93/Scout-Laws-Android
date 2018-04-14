package com.b4kancs.scoutlaws.views.quiz.chooser;


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

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.FragmentChooserBinding;
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment;
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceSharedViewModel;
import com.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseFragment;
import com.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseSharedViewModel;

import java.util.Random;

import static com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment.getMultipleChoiceFragmentTransaction;

/**
 * Created by hszilard on 25-Feb-18.
 * Choose a quiz type.
 */
public class ChooserFragment extends Fragment {
    public static final String FRAGMENT_TAG = "CHOOSER_FRAGMENT";
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
        transaction.addToBackStack(MultipleChoiceFragment.FRAGMENT_TAG);
        transaction.commit();
    }

    private void startPickAndChooseFragment() {
        PickAndChooseSharedViewModel pickAndChooseSharedViewModel = ViewModelProviders.of(getActivity())
                .get(PickAndChooseSharedViewModel.class);
        pickAndChooseSharedViewModel.reset();

        PickAndChooseFragment fragment = new PickAndChooseFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(container.getId(), fragment);
        transaction.addToBackStack(PickAndChooseFragment.FRAGMENT_TAG);
        transaction.commit();
    }

}
