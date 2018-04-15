package com.b4kancs.scoutlaws.views.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.data.model.ScoutLaw;
import com.b4kancs.scoutlaws.databinding.ActivityDetailsBinding;

/**
 * Created by hszilard on 21-Feb-18.
 */

public class DetailsActivity extends AppCompatActivity {
    public static final String SCOUT_LAW_INDEX_KEY = "INDEX";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private ActivityDetailsBinding binding;
    private DetailsActivityViewModel viewModel;

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
            index = bundle.getInt(SCOUT_LAW_INDEX_KEY);
        viewModel = ViewModelProviders.of(this, new DetailsActivityViewModelFactory(index))
                .get(DetailsActivityViewModel.class);

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setScoutLaw(viewModel.scoutLaw());
        binding.setIsModern(viewModel.isModern());
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
                return navigateUp();
            case R.id.contemporaryDesc_menuItem:
                Log.d(LOG_TAG, "Contemporary MenuItem selected.");
                viewModel.setModern(true);
                return true;
            case R.id.originalDesc_menuItem:
                Log.d(LOG_TAG, "Original MenuItem selected.");
                viewModel.setModern(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean navigateUp() {
        NavUtils.navigateUpFromSameTask(this);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        return true;
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }

    /* Set the scoutlaw's description based on a flag */
    @BindingAdapter({"descText_scoutLaw", "descText_isModern"})
    public static void setScoutLawDesc(@NonNull TextView textView, ScoutLaw scoutLaw, boolean isModern) {
        textView.setText(isModern ? scoutLaw.description : scoutLaw.originalDescription);

    }

    /* Set the description source based on a flag */
    @BindingAdapter("descSourceText_isModern")
    public static void setDescriptionSource(@NonNull TextView textView, boolean isModern) {
        Resources resources = textView.getResources();
        textView.setText(isModern ? resources.getString(R.string.source_modern) : resources.getString(R.string.source_orig));
    }
}
