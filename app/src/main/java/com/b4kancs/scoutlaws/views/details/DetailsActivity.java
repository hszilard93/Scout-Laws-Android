package com.b4kancs.scoutlaws.views.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityDetailsBinding;

import static com.b4kancs.scoutlaws.views.details.DetailsActivityViewModel.State;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;

/**
 * Created by hszilard on 21-Feb-18.
 */
public class DetailsActivity extends AppCompatActivity {
    public static final String SCOUT_LAW_NUMBER_KEY = "INDEX";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private ActivityDetailsBinding binding;
    private DetailsActivityViewModel viewModel;
    private boolean areAnimationsEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        /* DetailsActivity can be assumed to be created only via intents.
         * For now, we give index a default value instead of checking for invalid arguments. */
        int index = 0;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            index = bundle.getInt(SCOUT_LAW_NUMBER_KEY);
        viewModel = ViewModelProviders.of(this, new DetailsActivityViewModelFactory(index))
                .get(DetailsActivityViewModel.class);

        areAnimationsEnabled = areAnimationsEnabled(getApplicationContext());

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setScoutLaw(viewModel.scoutLaw());
        binding.setState(viewModel.observableState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(LOG_TAG, "Back navigation button pressed.");
                onBackPressed();
                return true;
            case R.id.contemporaryDesc_menuItem:
                Log.d(LOG_TAG, "Contemporary MenuItem selected.");
                viewModel.setState(State.MODERN);
                return true;
            case R.id.originalDesc_menuItem:
                Log.d(LOG_TAG, "Original MenuItem selected.");
                viewModel.setState(State.OLD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // This makes the shared element transition less cluttered
        if (areAnimationsEnabled) {
            binding.textNumber.setVisibility(View.GONE);
            binding.textModern.setVisibility(View.GONE);
            binding.textOld.setVisibility(View.GONE);
            binding.textSource.setVisibility(View.GONE);
        }
    }

    /* Must manually set up transitions because the default ones are inconsistent */
    @BindingAdapter("state_binding")
    public static void stateBindingAdapter(@NonNull ViewGroup layout, State state) {
        ActivityDetailsBinding binding = DataBindingUtil.findBinding(layout);
        TextView modern = binding.textModern;
        LinearLayout oldLayout = binding.linearOld;

        if (areAnimationsEnabled(layout.getContext()))
            TransitionManager.beginDelayedTransition(layout, new Slide());

        if (modern.getVisibility() == View.VISIBLE && state == State.OLD) {
            // change out TextViews
            modern.setVisibility(View.GONE);
            oldLayout.setVisibility(View.VISIBLE);
        } else if (oldLayout.getVisibility() == View.VISIBLE && state == State.MODERN) {
            // change out TextViews
            modern.setVisibility(View.VISIBLE);
            oldLayout.setVisibility(View.GONE);
        }

    }
}
