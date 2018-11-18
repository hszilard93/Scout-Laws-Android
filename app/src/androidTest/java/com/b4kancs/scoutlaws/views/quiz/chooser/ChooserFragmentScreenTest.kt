package com.b4kancs.scoutlaws.views.quiz.chooser

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.quiz.QuizActivity
import com.b4kancs.scoutlaws.views.quiz.QuizActivity.QUIZ_FRAGMENT_EXTRA
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.b4kancs.scoutlaws.pressUp

/**
 * Created by hszilard on 14-May-18.
 */
@RunWith(AndroidJUnit4::class)
class ChooserFragmentScreenTest {

    @get:Rule
    val quizActivityTestRule = object : ActivityTestRule<QuizActivity>(QuizActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val intent = Intent()
            intent.putExtra(QUIZ_FRAGMENT_EXTRA, ChooserFragment.FRAGMENT_TAG)
            return intent
        }
    }

    @Test
    fun clickingOnMultipleChoiceShouldStartMultipleChoiceFragment() {
        onView(withId(R.id.button_multiple))
                .perform(click())

        onView(withId(R.id.card_multiple))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnPickAndChooseShouldStartPickAndChooseFragment() {
        onView(withId(R.id.button_pick))
                .perform(click())

        onView(withId(R.id.card_pick))
                .check(matches(isDisplayed()))
    }

    @Test
    fun pressingUpShouldBringUpStartActivity() {
        pressUp()

        onView(withId(R.id.list_laws))
                .check(matches(isDisplayed()))
    }

}