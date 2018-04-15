package com.b4kancs.scoutlaws.views.quiz;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityQuizBinding;
import com.b4kancs.scoutlaws.views.quiz.chooser.ChooserFragment;

import java.util.List;

/**
 * Created by hszilard on 25-Feb-18.
 * This activity contains all the Quiz related fragments.
 */

public class QuizActivity extends AppCompatActivity {
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();

    private ActivityQuizBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ChooserFragment(), ChooserFragment.FRAGMENT_TAG)
                    .commit();
        }

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(LOG_TAG, "Up navigation button pressed.");
                return navigateUp();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean navigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            goToStartActivity();
        else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            Fragment sourceFragment = fragments.get(fragments.size() - 1);
            Fragment chooserFragment = new ChooserFragment();

            if (sourceFragment instanceof ChooserFragment) {
                goToStartActivity();
                return true;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(binding.fragmentContainer.getId(), chooserFragment);
            transaction.commit();
        }
        return true;
    }

    private void goToStartActivity() {
        NavUtils.navigateUpFromSameTask(this);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }
}
