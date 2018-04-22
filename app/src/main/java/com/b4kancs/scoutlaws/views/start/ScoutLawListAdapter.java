package com.b4kancs.scoutlaws.views.start;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.databinding.ListItemLawBinding;
import com.b4kancs.scoutlaws.views.details.DetailsActivity;

import java.util.ArrayList;

/**
 * Created by hszilard on 15-Feb-18.
 */
class ScoutLawListAdapter extends ArrayAdapter<ScoutLaw> implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = ScoutLawListAdapter.class.getSimpleName();

    private Activity activity;
    private ArrayList<ScoutLaw> scoutLaws;

    ScoutLawListAdapter(@NonNull ArrayList<ScoutLaw> scoutLaws, Activity activity) {
        super(activity, R.layout.list_item_law, scoutLaws);
        this.scoutLaws = scoutLaws;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView == null         // Do we have any old views to reuse?
                ? LayoutInflater.from(getContext()).inflate(R.layout.list_item_law, parent, false)
                : convertView;
        final ListItemLawBinding binding = DataBindingUtil.bind(view);
        binding.setScoutLaw(scoutLaws.get(position));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "List item clicked.");

        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(DetailsActivity.SCOUT_LAW_INDEX_KEY, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Set up shared element transition
        ListItemLawBinding binding = DataBindingUtil.bind(view);
        TextView textView = binding.mainTextView;
        ViewGroup layout = binding.listItemConstraintLayout;
        Pair<View, String> pair1 = Pair.create(textView, textView.getTransitionName());
        Pair<View, String> pair2 = Pair.create(layout, layout.getTransitionName());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pair1, pair2);

        activity.startActivity(intent, options.toBundle());
    }
}
