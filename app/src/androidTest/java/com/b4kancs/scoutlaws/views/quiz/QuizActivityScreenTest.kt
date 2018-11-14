package com.b4kancs.scoutlaws.views.quiz

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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