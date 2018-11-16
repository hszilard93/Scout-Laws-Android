package com.b4kancs.scoutlaws.views.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.databinding.ActivityQuizBinding;
import com.b4kancs.scoutlaws.views.BaseActivity;
import com.b4kancs.scoutlaws.views.quiz.chooser.ChooserFragment;
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment;
import com.b4kancs.scoutlaws.views.quiz.picker.PickerFragment;
import com.b4kancs.scoutlaws.views.quiz.sorter.SorterFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.util.Log.DEBUG;
import static android.util.Log.INFO;
import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;
import static com.crashlytics.android.Crashlytics.log;

/**
 * Created by hszilard on 25-Feb-18.
 * This activity contains all the Quiz related fragments.
 */
public class QuizActivity extends BaseActivity {
    public static final String QUIZ_FRAGMENT_EXTRA = "FRAGMENT_EXTRA";
    private static final String LOG_TAG = QuizActivity.class.getSimpleName();

    private ActivityQuizBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        log(INFO, LOG_TAG, "onCreate(..)");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);

        if (savedInstanceState == null) {
            log(DEBUG, LOG_TAG, "savedInstanceState == null");
            String fragmentTag = getIntent().getStringExtra(QUIZ_FRAGMENT_EXTRA);
            log(DEBUG, LOG_TAG, "fragmentTag == " + fragmentTag);
            if (MultipleChoiceFragment.FRAGMENT_TAG.equals(fragmentTag)
                    || PickerFragment.FRAGMENT_TAG.equals(fragmentTag)
                    || SorterFragment.FRAGMENT_TAG.equals(fragmentTag)) {
                startQuizShellFragment(fragmentTag);
            } else {
                log(DEBUG, LOG_TAG, "savedInstanceState != null;");
                startChooserFragment();
            }
        }

        setUpViews();
    }

    private void setUpViews() {
        log(DEBUG, LOG_TAG, "setUpViews()");
        setSupportActionBar((Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startQuizShellFragment(String fragmentTag) {
        log(DEBUG, LOG_TAG, "startQuizShellFragment(fragmentTag = " + fragmentTag + ")");
        Bundle args = new Bundle();
        args.putString("TAG", fragmentTag);
        QuizShellFragment quizShellFragment = new QuizShellFragment();
        quizShellFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, quizShellFragment, QuizShellFragment.FRAGMENT_TAG)
                .commit();
    }

    private void startChooserFragment() {
        log(DEBUG, LOG_TAG, "startChooserFragment()");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ChooserFragment(), ChooserFragment.FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                log(DEBUG, LOG_TAG, "Up navigation button pressed.");
                return navigateUp();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean navigateUp() {
        log(DEBUG, LOG_TAG, "navigateUp()");
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
        log(DEBUG, LOG_TAG, "goToStartActivity()");
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