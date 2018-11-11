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
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterFragment;

import java.util.List;

import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;

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
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        if (savedInstanceState == null) {
            String fragmentTag = getIntent().getStringExtra(QUIZ_FRAGMENT_EXTRA);
            if (MultipleChoiceFragment.FRAGMENT_TAG.equals(fragmentTag)
                    || PickAndChooseFragment.FRAGMENT_TAG.equals(fragmentTag)
                    || SorterFragment.FRAGMENT_TAG.equals(fragmentTag)) {
                startQuizShellFragment(fragmentTag);
            } else {
                startChooserFragment();
            }
        }

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startQuizShellFragment(String fragmentTag) {
        Bundle args = new Bundle();
        args.putString("TAG", fragmentTag);
        QuizShellFragment quizShellFragment = new QuizShellFragment();
        quizShellFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, quizShellFragment, QuizShellFragment.FRAGMENT_TAG)
                .commit();
    }

    private void startChooserFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ChooserFragment(), ChooserFragment.FRAGMENT_TAG)
                .commit();
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
            goToStartActivity();
            return true;
        }

        Fragment chooserFragment = new ChooserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ((QuizShellFragment) sourceFragment).triggerChildExitAnimation();
        if (areAnimationsEnabled(getApplicationContext()))
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(binding.fragmentContainer.getId(), chooserFragment);
        transaction.commit();

        return true;
    }

    private void goToStartActivity() {
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

        if (areAnimationsEnabled(getApplicationContext()))
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }
}
