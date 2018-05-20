package com.b4kancs.scoutlaws.views.quiz;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityQuizBinding;
import com.b4kancs.scoutlaws.views.quiz.chooser.ChooserFragment;
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment;
import com.b4kancs.scoutlaws.views.quiz.pickandchoose.PickAndChooseFragment;

import java.util.List;

/**
 * Created by hszilard on 25-Feb-18.
 * This activity contains all the Quiz related fragments.
 */
public class QuizActivity extends AppCompatActivity {
    public static final String QUIZ_FRAGMENT_EXTRA = "FRAGMENT_EXTRA";
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();

    private ActivityQuizBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        if (savedInstanceState == null) {
            String fragmentTag = getIntent().getStringExtra(QUIZ_FRAGMENT_EXTRA);
            if (fragmentTag != null) {
                switch (fragmentTag) {
                    case ChooserFragment.FRAGMENT_TAG:
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, new ChooserFragment(), ChooserFragment.FRAGMENT_TAG)
                                .commit();
                        break;
                    case MultipleChoiceFragment.FRAGMENT_TAG:
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, new MultipleChoiceFragment(), MultipleChoiceFragment.FRAGMENT_TAG)
                                .commit();
                        break;
                    case PickAndChooseFragment.FRAGMENT_TAG:
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container, new PickAndChooseFragment(), PickAndChooseFragment.FRAGMENT_TAG)
                                .commit();
                        break;
                }
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new ChooserFragment(), ChooserFragment.FRAGMENT_TAG)
                        .commit();
            }
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
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment sourceFragment = fragments.get(fragments.size() - 1);

        if (sourceFragment instanceof ChooserFragment) {
            navigateUpToStartActivity();
            return true;
        }

        Fragment chooserFragment = new ChooserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        ((QuizShellFragment)sourceFragment).triggerChildExitAnimation();
        transaction.replace(binding.fragmentContainer.getId(), chooserFragment);
        transaction.commit();

        return true;
    }

    private void navigateUpToStartActivity() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
