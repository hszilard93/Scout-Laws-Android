package com.b4kancs.scoutlaws.views.quiz.picker

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.view.View
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.quiz.QuizActivity
import com.b4kancs.scoutlaws.views.quiz.QuizActivity.QUIZ_FRAGMENT_EXTRA
import com.b4kancs.scoutlaws.countVisibleChildren
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by hszilard on 15-May-18.
 */
private fun extractView(matcher: Matcher<View>?): View? {
    var viewToReturn: View? = null

    onView(matcher).perform(object : ViewAction {
        override fun getDescription(): String {
            return "Extracting View from matcher."
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(View::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            viewToReturn = view
        }
    })
    return viewToReturn
}

@RunWith(AndroidJUnit4::class)
class PickerFragmentScreenTest {

    @get:Rule
    val quizActivityTestRule = object : ActivityTestRule<QuizActivity>(QuizActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val intent = Intent()
            intent.putExtra(QUIZ_FRAGMENT_EXTRA, PickerFragment.FRAGMENT_TAG)
            return intent
        }
    }

    private fun insertAnswers(vararg answers: String) {
        /* Couldn't do drag & drop with Espresso, using the ViewModel instead to insert answers */
        /* Get access to the private viewModel using reflection */
        val quizShellFragment = (quizActivityTestRule.activity as QuizActivity)
                .supportFragmentManager
                .fragments
                .last()
        val pickChooseFragment = quizShellFragment.childFragmentManager.fragments.last()

        val viewModelField = PickerFragment::class.java.getDeclaredField("viewModel")
        viewModelField.isAccessible = true
        val viewModel = viewModelField.get(pickChooseFragment) as PickerViewModel

        /* Insert correct answers into userAnswers*/
        viewModel.apply {
            for ((i, key) in userAnswers.navigableKeySet().withIndex())
                addUserAnswer(key, answers[i])
        }
    }

    @Test
    fun correctViewShouldBeDisplayedAfterStarted() {
        onView(withId(R.id.constraint_top_bar))
                .check(matches(isDisplayed()))

        onView(withId(R.id.flow_question))
                .check(matches(isDisplayed()))

        onView(withId(R.id.flow_options))
                .check(matches(isDisplayed()))

        onView(withId(R.id.button_help))
                .check(matches(isDisplayed()))

        onView(withId(R.id.button_clear))
                .check(matches(isDisplayed()))

        onView(withId(R.id.button_give_up))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.button_check))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.button_next))
                .check(matches(not(isDisplayed())))

        onView(withId(R.id.button_finish))
                .check(matches(not(isDisplayed())))
    }

    @Test
    fun correctAndIncorrectAnswersShouldBeShownAfterStarted() {
        onView(withText("correct1"))
                .check(matches(isDisplayed()))

        onView(withText("correct2"))
                .check(matches(isDisplayed()))

        onView(withText("option1"))
                .check(matches(isDisplayed()))

        onView(withText("option2"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingHelpShouldMakeIncorrectOptionsDisappear() {
        val numberOfVisibleChildrenBefore = countVisibleChildren(withId(R.id.flow_options))

        onView(withId(R.id.button_help))
                .perform(click())
        val numberOfVisibleChildrenAfter = countVisibleChildren(withId(R.id.flow_options))

        assertTrue(numberOfVisibleChildrenAfter < numberOfVisibleChildrenBefore)
        onView(withText("correct1"))
                .check(matches(isDisplayed()))
        onView(withText("correct2"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingHelpAtMostThreeTimesShouldShowGiveUpButton() {
        /* Click help 3 times or while it is shown */
        val helpButton = extractView(withId(R.id.button_help))
        var helpShown = helpButton!!.isShown
        var clicked = 0
        while (helpShown && clicked++ < 3) {
            onView(withId(R.id.button_help))
                    .perform(click())
            helpShown = helpButton.isShown
        }

        assertFalse(helpShown)
        onView(withId(R.id.button_give_up))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingGiveUpShouldShowNextButtonAndFillInCorrectAnswers() {
        /* Click help 3 times or while it is shown */
        val helpButton = extractView(withId(R.id.button_help))
        var helpShown = helpButton!!.isShown
        var clicked = 0
        while (helpShown && clicked++ < 3) {
            onView(withId(R.id.button_help))
                    .perform(click())
            helpShown = helpButton.isShown
        }

        /* Help is now replaced by Give Up */
        onView(withId(R.id.button_give_up))
                .perform(click())

        /* Give Up is replaced by Next */
        onView(withId(R.id.button_next))
                .check(matches(isDisplayed()))
        /* Correct answers should be filled in */
        onView(allOf(
                withText("correct1"),
                isDescendantOfA(withId(R.id.flow_question))
        ))
                .check(matches(isDisplayed()))
        onView(allOf(
                withText("correct2"),
                isDescendantOfA(withId(R.id.flow_question))
        ))
                .check(matches(isDisplayed()))
    }

    @Test
    fun insertingCorrectAnswersShouldShowCheckButton() {
        insertAnswers("correct1", "correct2")
        Thread.sleep(50)    // Sometimes the UI doesn't refresh in time, must wait

        onView(withId(R.id.button_check))
                .check(matches(isDisplayed()))
    }

    @Test
    fun insertingAnswersShouldRemoveThemFromOptions() {
        insertAnswers("option1", "correct1")
        Thread.sleep(50)    // Sometimes the UI doesn't refresh in time, must wait

        onView(allOf(
                withText("option1"),
                isDescendantOfA(withId(R.id.flow_options))
        ))
                .check(doesNotExist())
    }

    @Test
    fun clickingCheckButtonShouldRemoveIncorrectAnswerFromQuestionFlowAndAddItToOptionsFlow() {
        insertAnswers("correct1", "option1")
        // Sometimes the UI doesn't refresh in time, must wait
        Thread.sleep(50)
        onView(withId(R.id.button_check))
                .perform(click())

        onView(allOf(
                withText("correct1"),
                isDescendantOfA(withId(R.id.flow_question))
        ))
                .check(matches(isDisplayed()))
        onView(allOf(
                withText("option1"),
                isDescendantOfA(withId(R.id.flow_question))
        ))
                .check(doesNotExist())
        onView(allOf(
                withText("option1"),
                isDescendantOfA(withId(R.id.flow_options))
        ))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingCheckButtonShouldShowNextButtonWhenCorrect() {
        insertAnswers("correct1", "correct2")
        // Sometimes the UI doesn't refresh in time, must wait
        Thread.sleep(50)
        onView(withId(R.id.button_check))
                .perform(click())

        onView(withId(R.id.button_next))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingNextButtonShouldBringUpNextQuestion() {
        val previousQuestionFlowObject = extractView(withId(R.id.flow_question))

        insertAnswers("correct1", "correct2")
        // Sometimes the UI doesn't refresh in time, must wait
        Thread.sleep(50)
        onView(withId(R.id.button_check))
                .perform(click())
        onView(withId(R.id.button_next))
                .perform(click())

        val currentQuestionFlowObject = extractView(withId(R.id.flow_question))
        assertNotEquals(previousQuestionFlowObject, currentQuestionFlowObject)
    }

    @Test
    fun finishingQuizShouldBringUpResultsDialog() {
        for (i in 1..4) {
            insertAnswers("correct1", "correct2")
            Thread.sleep(50)    // Sometimes the UI doesn't refresh in time, must wait
            onView(withId(R.id.button_check))
                    .perform(click())
            Thread.sleep(50)
            onView(withId(R.id.button_next))
                    .perform(click())
        }
        insertAnswers("correct1", "correct2")
        Thread.sleep(50)
        onView(withId(R.id.button_check))
                .perform(click())
        Thread.sleep(50)
        onView(withId(R.id.button_finish))
                .perform(click())

        onView(withId(R.id.layout_stars))
                .check(matches(isDisplayed()))
    }
}
