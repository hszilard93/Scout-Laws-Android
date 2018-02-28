package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;


import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.FragmentMultipleBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultipleChoiceFragment extends Fragment {
    private static final String LOG_TAG = MultipleChoiceFragment.class.getSimpleName();

    private MultipleChoiceSharedViewModel viewModel;
    private FragmentMultipleBinding binding;
    private ViewGroup container;

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MultipleChoiceSharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple, container, false);
        binding.setViewModel(viewModel);
        setRetainInstance(true);

        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        OptionsListAdapter listAdapter = new OptionsListAdapter(viewModel, getContext(), onOptionSelected);
        binding.optionsLinearLayout.setAdapter(listAdapter);
        binding.optionsLinearLayout.setOnItemClickListener(listAdapter.defaultItemClickListener);
        binding.nextButton.setOnClickListener(nextButtonClickedListener);
    }

    private OptionsListAdapter.OptionSelectedCallback onOptionSelected = (adapter, view, scoutLaw) -> {
        if (viewModel.isNewAnswerCorrect(scoutLaw)) {
            Toast toast = Toast.makeText(getContext(), "A válasz helyes!", Toast.LENGTH_SHORT);
            toast.show();
            endTurn(adapter);
        }
        else {
            Toast toast = Toast.makeText(getContext(), "A válasz helytelen!", Toast.LENGTH_SHORT);
            toast.show();
            view.setVisibility(View.GONE);
            if (viewModel.turnOver.get()) {
                endTurn(adapter);
            }
        }
    };

    private View.OnClickListener nextButtonClickedListener = (item) -> {
        Log.d(LOG_TAG, "Next button clicked.");
        transitionToNextQuestion();
    };

    private void endTurn(OptionsListAdapter adapter) {
        binding.optionsLinearLayout.setOnItemClickListener(adapter.disabledItemClickListener);
    }

    private void transitionToNextQuestion() {
        binding.unbind();
        viewModel.startNewTurn();
        MultipleChoiceFragment newFragment = new MultipleChoiceFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        transaction.replace(container.getId(), newFragment);
        transaction.commit();
    }

    @BindingAdapter("nextButton_turnOver")
    public static void setNextButtonTurnOver(@NonNull Button button, boolean turnOver) {
        Resources resources = button.getResources();
        if (turnOver) {
            button.setEnabled(true);
            button.setTextColor(resources.getColor(R.color.colorPrimary));
            button.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, resources.getDrawable(R.drawable.ic_keyboard_arrow_right_green_24dp), null);
        }
        else {
            button.setEnabled(false);
            button.setTextColor(resources.getColor(R.color.disabled_grey));
            button.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, resources.getDrawable(R.drawable.ic_keyboard_arrow_right_grey_24dp), null);
        }
    }
}
