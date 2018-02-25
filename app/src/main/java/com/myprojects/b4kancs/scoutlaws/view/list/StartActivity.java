package com.myprojects.b4kancs.scoutlaws.view.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.databinding.StartActivityBinding;

/**
 * Created by hszilard on 15-Feb-18.
 * This is the starting activity.
 */

public class StartActivity extends AppCompatActivity {
    public static final String LOG_TAG = StartActivity.class.getSimpleName();

    private StartActivityBinding binding;
    private StartActivityViewModel viewModel;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.start_activity);
        viewModel = ViewModelProviders.of(this).get(StartActivityViewModel.class);

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);

        ScoutLawListAdapter listAdapter = new ScoutLawListAdapter(viewModel.scoutLaws(), getApplicationContext());
        binding.lawsListView.setAdapter(listAdapter);
        binding.lawsListView.setOnItemClickListener(listAdapter);
        /* This empty view gives the last list item space for its shadow */
        TextView empty = new TextView(this);
        empty.setHeight(1);
        binding.lawsListView.addFooterView(empty);

        drawerToggle = setUpDrawerToggle();
        setUpDrawerContent();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
            return new ActionBarDrawerToggle(this, binding.drawerLayout, (Toolbar) binding.toolbar,
                R.string.drawer_open,  R.string.drawer_close);
    }

    private void setUpDrawerContent() {
        binding.nvView.setNavigationItemSelectedListener(item -> {
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
