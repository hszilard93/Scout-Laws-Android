package com.b4kancs.scoutlaws.views.quiz.chooser;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.FragmentChooserBinding;
import com.b4kancs.scoutlaws.views.quiz.QuizShellFragment;
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment;
import com.b4kancs.scoutlaws.views.quiz.picker.PickerFragment;
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterFragment;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.views.quiz.CommonQuizUtils.getFragmentTransaction;
import static com.crashlytics.android.Crashlytics.log;

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
        log(INFO, LOG_TAG, "onCreateView(..)");
        this.container = container;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chooser, container, false);
        setUpViews();

        setRetainInstance(true);
        return binding.getRoot();
    }

    private void setUpViews() {
        log(DEBUG, LOG_TAG, "setUpViews()");
        binding.buttonAny.setOnClickListener(click -> {
            log(INFO, LOG_TAG, "Any quiz button pressed.");
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
                startQuiz(MultipleChoiceFragment.FRAGMENT_TAG);
            else
                startQuiz(PickerFragment.FRAGMENT_TAG);
        });
        binding.buttonMultiple.setOnClickListener(click -> {
            log(INFO, LOG_TAG, "Multiple choice button pressed.");
            startQuiz(MultipleChoiceFragment.FRAGMENT_TAG);
        });
        binding.buttonPick.setOnClickListener(click -> {
            log(INFO, LOG_TAG, "Pick and choose button pressed.");
            startQuiz(PickerFragment.FRAGMENT_TAG);
        });
        binding.buttonSorter.setOnClickListener(click -> {
            log(INFO, LOG_TAG, "Sorter button pressed.");
            startQuiz(SorterFragment.FRAGMENT_TAG);
        });
    }

    private void startQuiz(final String fragmentTag) {
        log(INFO, LOG_TAG, "startQuiz(fragmentTag = " + fragmentTag + ")");
        Bundle args = new Bundle();
        args.putString("TAG", fragmentTag);
        QuizShellFragment quizShellFragment = new QuizShellFragment();
        quizShellFragment.setArguments(args);

        FragmentTransaction transaction = getFragmentTransaction(container, getFragmentManager(), quizShellFragment);
        transaction.addToBackStack(QuizShellFragment.FRAGMENT_TAG);
        transaction.commit();
    }
}
