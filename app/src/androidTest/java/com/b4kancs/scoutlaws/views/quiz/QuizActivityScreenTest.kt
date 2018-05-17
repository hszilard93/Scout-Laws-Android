package com.b4kancs.scoutlaws.views.quiz

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.start.StartActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by hszilard on 13-May-18.
 */
@RunWith(AndroidJUnit4::class)
class QuizActivityScreenTest {

    @get:Rule
    val quizActivityTestRule = ActivityTestRule<QuizActivity>(QuizActivity::class.java)

    @Test
    fun quizActivityShouldStartWithChooserFragmentByDefault() {
        onView(withId(R.id.constraint_chooser))
                .check(matches(isDisplayed()))
    }
}