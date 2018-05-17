package com.b4kancs.scoutlaws.views.start;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityStartBinding;
import com.b4kancs.scoutlaws.views.quiz.QuizActivity;

/**
 * Created by hszilard on 15-Feb-18.
 * This is the starting activity.
 */

public class StartActivity extends AppCompatActivity {
    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    private ActivityStartBinding binding;
    private StartActivityViewModel viewModel;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        viewModel = ViewModelProviders.of(this).get(StartActivityViewModel.class);
        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);

        ScoutLawListAdapter listAdapter = new ScoutLawListAdapter(viewModel.scoutLaws(), this);
        binding.listLaws.setAdapter(listAdapter);
        binding.listLaws.setOnItemClickListener(listAdapter);
        /* This empty view gives the last list item space for its shadow */
        TextView empty = new TextView(this);
        empty.setHeight(1);
        binding.listLaws.addFooterView(empty);
        binding.listLaws.addHeaderView(empty);

        drawerToggle = setUpDrawerToggle();
        setUpDrawerContent();
        NavigationView navigationView = binding.navigationView;
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_quiz:
                    Log.d(LOG_TAG, "Quiz menu item selected.");
                    startQuizActivity();
                    return true;
                default:
                    return false;
            }
        });
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

    private void startQuizActivity() {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
            return new ActionBarDrawerToggle(this, binding.drawerLayout, (Toolbar) binding.toolbar,
                R.string.drawer_open,  R.string.drawer_close);
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
