package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

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
import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.myprojects.b4kancs.scoutlaws.databinding.ListItemOptionBinding;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class OptionsListAdapter extends ArrayAdapter<ScoutLaw> {
    private static final String LOG_TAG = OptionsListAdapter.class.getSimpleName();

    private Context context;
    private OptionSelectedCallback callback;
    private MultipleChoiceSharedViewModel viewModel;
    private int lastPosition = -1;

    public OptionsListAdapter(@NonNull MultipleChoiceSharedViewModel viewModel, Context context, OptionSelectedCallback callback) {
        super(context, R.layout.list_item_law, viewModel.getOptions());
        this.context = context;
        this.viewModel = viewModel;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView == null         // Do we have any old views to reuse?
                ? LayoutInflater.from(getContext()).inflate(R.layout.list_item_option, parent, false)
                : convertView;
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

    @BindingAdapter({"optionBackground_correctGuessed", "optionBackground_isCorrect"})
    public static void setOptionViewBackground(@NonNull View view, boolean correctGuessed,
                                               boolean isCorrect) {
        Resources resources = view.getResources();
        view.setBackgroundColor(resources.getColor(correctGuessed && !isCorrect
                ? R.color.disabled_grey
                : R.color.colorPrimary));
    }
}
