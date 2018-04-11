package com.b4kancs.scoutlaws.views.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.b4kancs.scoutlaws.R;
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
        viewModel.init();

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
                onBackPressed();
                return true;
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
}
