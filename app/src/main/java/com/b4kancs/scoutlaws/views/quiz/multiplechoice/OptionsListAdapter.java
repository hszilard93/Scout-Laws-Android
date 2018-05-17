package com.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linearlistview.LinearListView;
import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.databinding.ListItemOptionBinding;

/**
 * Created by hszilard on 15-Feb-18.
 * List adapter for the list of possible answers.
 */

public class OptionsListAdapter extends ArrayAdapter<ScoutLaw> {
    private static final String LOG_TAG = OptionsListAdapter.class.getSimpleName();

    private OptionSelectedCallback callback;
    private MultipleChoiceViewModel viewModel;

    OptionsListAdapter(@NonNull MultipleChoiceViewModel viewModel, Context context, OptionSelectedCallback callback) {
        super(context, R.layout.list_item_law, viewModel.getOptions());
        this.viewModel = viewModel;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView == null         // Do we have any old views to reuse?
                ? LayoutInflater.from(getContext()).inflate(R.layout.list_item_option, parent, false)
                : convertView;
        view.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        final ListItemOptionBinding binding = DataBindingUtil.bind(view);
        binding.setScoutLaw(viewModel.getOptions().get(position));
        binding.setViewModel(viewModel);

        return view;
    }

    LinearListView.OnItemClickListener defaultItemClickListener = (parent, view, position, id) -> {
        Log.d(LOG_TAG, "List item clicked.");
        callback.onOptionSelected(this, view, viewModel.getOptions().get(position));
    };
    LinearListView.OnItemClickListener disabledItemClickListener = (parent, view, position, id) -> {};

    interface OptionSelectedCallback {
        void onOptionSelected(OptionsListAdapter adapter, View view, ScoutLaw scoutLaw);
    }

    /* When the correct answer is selected, disable the other possible answers */
    @BindingAdapter({"optionBackground_correctGuessed", "optionBackground_isCorrect"})
    public static void setOptionViewBackground(@NonNull View view, MultipleChoiceViewModel.State state,
                                               boolean isThisOptionCorrect) {
        Resources resources = view.getResources();
        view.setBackgroundColor(resources.getColor(state == MultipleChoiceViewModel.State.DONE && !isThisOptionCorrect
                ? R.color.disabled_grey
                : R.color.colorPrimary));
    }
}
