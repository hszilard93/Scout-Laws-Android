package com.myprojects.b4kancs.scoutlaws.view.list;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.myprojects.b4kancs.scoutlaws.databinding.LawListItemBinding;
import com.myprojects.b4kancs.scoutlaws.view.details.DetailsActivity;

import java.util.ArrayList;

/**
 * Created by hszilard on 15-Feb-18.
 */

public class ScoutLawListAdapter extends ArrayAdapter<ScoutLaw> implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = ScoutLawListAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<ScoutLaw> scoutLaws;
    private int lastPosition = -1;

    public ScoutLawListAdapter(@NonNull ArrayList<ScoutLaw> scoutLaws, Context context) {
        super(context, R.layout.law_list_item, scoutLaws);
        this.scoutLaws = scoutLaws;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView == null         // Do we have any old views to reuse?
                ? LayoutInflater.from(getContext()).inflate(R.layout.law_list_item, parent, false)
                : convertView;
        final LawListItemBinding binding = DataBindingUtil.bind(view);
        binding.setScoutLaw(scoutLaws.get(position));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "List item clicked.");

        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsActivity.SCOUT_LAW_INDEX_KEY, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent );
    }
}
