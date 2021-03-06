package com.b4kancs.scoutlaws.views.start;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.b4kancs.scoutlaws.App;
import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityStartBinding;
import com.b4kancs.scoutlaws.views.BaseActivity;
import com.b4kancs.scoutlaws.views.menu.AboutDialogFragment;
import com.b4kancs.scoutlaws.views.menu.PrivacyDialogFragment;
import com.b4kancs.scoutlaws.views.menu.StatsDialogFragment;
import com.b4kancs.scoutlaws.views.quiz.QuizActivity;
import com.b4kancs.scoutlaws.views.settings.PreferencesActivity;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;
import javax.inject.Named;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.logger.Logger.log;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.isPastelEnabled;

/**
 * Created by hszilard on 15-Feb-18.
 * This is the starting activity.
 */
public class StartActivity extends BaseActivity {
    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    ActivityStartBinding binding;
    @Inject @Named("release_notes") protected boolean shouldShowReleaseNotes;
    private StartActivityViewModel viewModel;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log(INFO, LOG_TAG, "onCreate(..)");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        viewModel = ViewModelProviders.of(this).get(StartActivityViewModel.class);
        setUpViews();

        // Fixes screen flashing during shared element transition
        {
            Fade fade = new Fade();
            fade.excludeTarget(R.id.toolbar, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        if (shouldShowReleaseNotes)
            showReleaseNotes();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        log(Log.DEBUG, LOG_TAG, "onConfigurationChanged(..)");
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            log(DEBUG, LOG_TAG, "Drawer toggle clicked.");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpViews() {
        log(Log.DEBUG, LOG_TAG, "setUpViews()");

        setSupportActionBar((Toolbar) binding.toolbar);

        ScoutLawListAdapter listAdapter = new ScoutLawListAdapter(viewModel.scoutLaws(), this);
        binding.listLaws.setAdapter(listAdapter);
        binding.listLaws.setOnItemClickListener(listAdapter);
        /* Add header and footer for looks */
        TextView empty = new TextView(this);
        empty.setHeight(0);
        binding.listLaws.addFooterView(empty);
        binding.listLaws.addHeaderView(empty);

        drawerToggle = setUpDrawerToggle();
        setUpDrawerContent();
        NavigationView navigationView = binding.navigationView;
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelected);

        if (isPastelEnabled(getApplicationContext())) {
            navigationView.getHeaderView(0).setBackgroundColor(
                    getResources().getColor(R.color.colorNavHeaderLight)
            );
        }
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelected = item -> {
        switch (item.getItemId()) {
            case R.id.menu_item_quiz:
                log(INFO, LOG_TAG, "Quiz menu item selected.");
                startActivity(QuizActivity.class);
                return true;
            case R.id.menu_item_settings:
                log(INFO, LOG_TAG, "Settings menu item selected.");
                startActivity(PreferencesActivity.class);
                return true;
            case R.id.menu_item_stats:
                log(INFO, LOG_TAG, "Stats menu item selected.");
                new StatsDialogFragment().show(getSupportFragmentManager());
                return true;
            case R.id.menu_item_about:
                log(INFO, LOG_TAG, "About menu item selected.");
                new AboutDialogFragment().show(getSupportFragmentManager());
                return true;
            case R.id.menu_item_privacy:
                log(INFO, LOG_TAG, "Privacy menu item selected.");
                new PrivacyDialogFragment().show(getSupportFragmentManager());
                return true;
            default:
                return false;
        }
    };

    private void startActivity(Class activityClass) {
        log(INFO, LOG_TAG, "startActivity(..); new activity = " + activityClass.getSimpleName());
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        if (areAnimationsEnabled(getApplicationContext()))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, binding.drawerLayout, (Toolbar) binding.toolbar,
                R.string.drawer_open, R.string.drawer_close);
    }

    private void setUpDrawerContent() {
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    private void selectDrawerItem(MenuItem item) {
        log(DEBUG, LOG_TAG, item.getItemId() + " drawer item selected.");

        item.setChecked(true);
        binding.drawerLayout.closeDrawers();
    }

    private void showReleaseNotes() {
        log(INFO, LOG_TAG, "showReleaseNotes()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle(getResources().getString(R.string.version_17_release_title))
                .setMessage(getResources().getString(R.string.version_17_release_notes))
                .setPositiveButton(getResources().getString(R.string.ok_button), null)
                .setCancelable(false)
                .create();
        dialog.setOnShowListener(dialog1 ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary)));
        dialog.show();
    }
}
