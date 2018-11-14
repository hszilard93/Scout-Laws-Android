package com.b4kancs.scoutlaws.views.start;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityStartBinding;
import com.b4kancs.scoutlaws.views.quiz.QuizActivity;
import com.b4kancs.scoutlaws.views.settings.PreferencesActivity;

import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;

/**
 * Created by hszilard on 15-Feb-18.
 * This is the starting activity.
 */
public class StartActivity extends AppCompatActivity {
    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    ActivityStartBinding binding;
    private StartActivityViewModel viewModel;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

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
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            Log.d(LOG_TAG, "Drawer toggle clicked.");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpViews() {
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
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelected = item -> {
        switch (item.getItemId()) {
            case R.id.menu_item_quiz:
                Log.d(LOG_TAG, "Quiz menu item selected.");
                startActivity(QuizActivity.class);
                return true;
            case R.id.menu_item_settings:
                Log.d(LOG_TAG, "Settings menu item selected.");
                startActivity(PreferencesActivity.class);
                return true;
            case R.id.menu_item_about:
                new AboutDialogFragment().show(getSupportFragmentManager());
                //NotificationUtilsKt.showQuizPromptNotification(this);
            default:
                return false;
        }
    };

    private void startActivity(Class activityClass) {
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
        Log.d(LOG_TAG, item.getItemId() + " item selected.");

        item.setChecked(true);
        binding.drawerLayout.closeDrawers();
    }
}