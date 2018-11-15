package com.b4kancs.scoutlaws.views.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityDetailsBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.views.details.DetailsActivityViewModel.State;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;
import static com.crashlytics.android.Crashlytics.log;

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
        log(DEBUG, LOG_TAG, "onCreate(..)");
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
        log(INFO, LOG_TAG, "Scout law index is: " + index);
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
        log(DEBUG, LOG_TAG, "setUpViews()");
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setScoutLaw(viewModel.scoutLaw());
        binding.setState(viewModel.observableState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        log(DEBUG, LOG_TAG, "onCreateOptionsMenu(..)");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log(DEBUG, LOG_TAG, "onOptionsItemSelected(..)");
        switch (item.getItemId()) {
            case android.R.id.home:
                log(DEBUG, LOG_TAG, "Back navigation button pressed.");
                onBackPressed();
                return true;
            case R.id.contemporaryDesc_menuItem:
                log(DEBUG, LOG_TAG, "Contemporary MenuItem selected.");
                viewModel.setState(State.MODERN);
                return true;
            case R.id.originalDesc_menuItem:
                log(DEBUG, LOG_TAG, "Original MenuItem selected.");
                viewModel.setState(State.OLD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        log(DEBUG, LOG_TAG, "onBackPressed(..)");
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
