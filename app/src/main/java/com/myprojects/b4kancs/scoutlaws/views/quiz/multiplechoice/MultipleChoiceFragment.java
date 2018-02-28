package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;


import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.FragmentMultipleBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultipleChoiceFragment extends Fragment {
    private static final String LOG_TAG = MultipleChoiceFragment.class.getSimpleName();

    private MultipleChoiceFragmentViewModel viewModel;
    private FragmentMultipleBinding binding;

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(MultipleChoiceFragmentViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple, container, false);
        binding.setViewModel(viewModel);
        setRetainInstance(true);

        setUpViews();

        return binding.getRoot();
    }

    private void setUpViews() {
        OptionsListAdapter listAdapter = new OptionsListAdapter(viewModel.getOptions(), getContext());
        binding.optionsListView.setAdapter(listAdapter);
        binding.optionsListView.setOnItemClickListener(listAdapter);
        TextView empty = new TextView(getContext());
        empty.setHeight(0);
        binding.optionsListView.addFooterView(empty);
    }
}
