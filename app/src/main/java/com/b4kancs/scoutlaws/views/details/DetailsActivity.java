package com.b4kancs.scoutlaws.views.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);


        /* DetailsActivity can be assumed to be created only via intents. */
        int index = 0;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            index = bundle.getInt(SCOUT_LAW_NUMBER_KEY, 0);
        }
        viewModel = ViewModelProviders.of(this, new DetailsActivityViewModelFactory(index))
                .get(DetailsActivityViewModel.class);

        setUpViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(R.id.toolbar, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
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
        if (areAnimationsEnabled(getApplicationContext())) {
            binding.textNumber.setVisibility(View.GONE);
            binding.textModern.setVisibility(View.GONE);
            binding.textOld.setVisibility(View.GONE);
            binding.textSource.setVisibility(View.GONE);
        }
    }
}
