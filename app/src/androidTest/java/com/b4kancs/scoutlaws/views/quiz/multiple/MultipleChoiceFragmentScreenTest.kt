package com.b4kancs.scoutlaws.views.quiz.multiple

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.quiz.QuizActivity
import com.b4kancs.scoutlaws.views.quiz.QuizActivity.QUIZ_FRAGMENT_EXTRA
import com.b4kancs.scoutlaws.views.quiz.multiplechoice.MultipleChoiceFragment
import com.b4kancs.scoutlaws.countVisibleChildren
import com.b4kancs.scoutlaws.extractText
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.b4kancs.scoutlaws.pressUp

/**
 * Created by hszilard on 14-May-18.
 */

/* Based on https://stackoverflow.com/a/36866682/4449809 */
fun <T> first(matcher: Matcher<T>): Matcher<T> {
    return object : BaseMatcher<T>() {
        var isFirst = true

        override fun describeTo(description: Description?) {
            description?.appendText("Return first matching item.")
        }

        override fun matches(item: Any?): Boolean {
            if (isFirst && matcher.matches(item)) {
                isFirst = false
                return true
            }
            return false
        }
    }
}

fun clickCorrectAnswer() {
    val text = extractText(withId(R.id.text_question))
    val matcher = """(\d+)""".toPattern().matcher(text)
    matcher.find()
    val index = matcher.group().toInt()

    onView(withText("Scout law $index test value."))
            .perform(click())
}

fun clickIncorrectAnswer() {
    val text = extractText(withId(R.id.text_question))
    val matcher = """(\d+)""".toPattern().matcher(text)
    matcher.find()
    val correctIndex = matcher.group().toInt()

    onView(first(allOf(
            not(withText("Scout law $correctIndex test value.")),
            withId(R.id.text_law),
            isDisplayed()
    )))
            .perform(click())
}

@RunWith(AndroidJUnit4::class)
class MultipleChoiceFragmentScreenTest {

    @get:Rule
    val quizActivityTestRule = object : ActivityTestRule<QuizActivity>(QuizActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val intent = Intent()
            intent.putExtra(QUIZ_FRAGMENT_EXTRA, MultipleChoiceFragment.FRAGMENT_TAG)
            return intent
        }
    }

    @Test
    fun correctViewsShouldBeDisplayedAfterStarted() {
        onView(withId(R.id.constraint_top_bar))
                .check(matches(isDisplayed()))

        onView(withId(R.id.text_question))
                .check(matches((isDisplayed())))

        onView(withId(R.id.button_next))
                .check(matches(not(isEnabled())))

        val numberOfVisibleChildren = countVisibleChildren(withId(R.id.linear_options))
        assertEquals(4, numberOfVisibleChildren)
    }

    @Test
    fun selectingCorrectAnswerShouldEnableNextButton() {
        clickCorrectAnswer()

        onView(withId(R.id.button_next))
                .check(matches(isEnabled()))
    }

    @Test
    fun selectingIncorrectAnswerShouldMakeItDisappear() {
        clickIncorrectAnswer()

        val numberOfVisibleChildren = countVisibleChildren(withId(R.id.linear_options))
        assertEquals(3, numberOfVisibleChildren)
    }

    @Test
    fun selectingIncorrectAnswerThreeTimesShouldEnableNextButton() {
        for (i in 1..3)
            clickIncorrectAnswer()

        onView(withId(R.id.button_next))
                .check(matches(isClickable()))
    }

    @Test
    fun clickingNextButtonShouldBringUpNextQuestion() {
        val oldQuestion = extractText(withId(R.id.text_question))

        clickCorrectAnswer()
        onView(withId(R.id.button_next))
                .perform(click())

        val newQuestion = extractText(withId(R.id.text_question))
        assertNotEquals(oldQuestion, newQuestion)
    }

    @Test
    fun clickingThroughFiveQuestionsAndFinishShouldShowResultScreen() {
        for (i in 1..4) {
            clickCorrectAnswer()
            onView(withId(R.id.button_next))
                    .perform(click())
        }
        clickCorrectAnswer()
        onView(withId(R.id.button_finish))
                .perform(click())

        onView(withId(R.id.image_star1))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_total_score))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_time))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingTheUpButtonShouldBringUpChooserFragment() {
        /* First go one question deep */
        clickCorrectAnswer()
        onView(withId(R.id.button_next)).perform(click())
        pressUp()

        onView(withId(R.id.card_chooser))
                .check(matches(isDisplayed()))
    }

    @Test
    fun chronometerShouldTickAfterStarted() {
        val chronoText1 = extractText(withId(R.id.chronometer))
        Thread.sleep(1000)
        val chronoText2 = extractText(withId(R.id.chronometer))

        assertNotEquals(chronoText1, chronoText2)
    }

    @Test
    fun chronometerShouldStopAfterFiveQuestionsAnswered() {
        for (i in 1..4) {
            clickCorrectAnswer()
            onView(withId(R.id.button_next))
                    .perform(click())
        }
        clickCorrectAnswer()

        val chronoText1 = extractText(withId(R.id.chronometer))
        Thread.sleep(1000)
        val chronoText2 = extractText(withId(R.id.chronometer))

        assertEquals(chronoText1, chronoText2)
    }
}
