package com.myprojects.b4kancs.scoutlaws.view.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.DetailsActivityBinding;

/**
 * Created by hszilard on 21-Feb-18.
 */

public class DetailsActivity extends AppCompatActivity {
    public static final String SCOUT_LAW_INDEX_KEY = "INDEX";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private DetailsActivityBinding binding;
    private DetailsActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.details_activity);
        /* For now, give index a default value instead of checking for error */
        int index = 0;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            index = bundle.getInt(SCOUT_LAW_INDEX_KEY);
        viewModel = ViewModelProviders.of(this, new DetailsActivityViewModelFactory(this.getApplication(), index))
                .get(DetailsActivityViewModel.class);

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setScoutLaw(viewModel.scoutLaw());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(LOG_TAG, "Back navigation button pressed.");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
