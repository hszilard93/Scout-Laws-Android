package com.b4kancs.scoutlaws.views.start

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.contrib.DrawerMatchers.isOpen
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.Gravity
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.data.model.ScoutLaw
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by hszilard on 16-May-18.
 */
@RunWith(AndroidJUnit4::class)
class StartActivityScreenTest {
    @get:Rule
    val quizActivityTestRule = ActivityTestRule<StartActivity>(StartActivity::class.java)

    @Test
    fun allScoutLawsShouldBeShown() {
        /* ListView items are displayed correctly */
        onView(withText("Scout law 1 test value."))
                .check(matches(isDisplayed()))

        /* All ListView items are displayed */
        for (i in 1..10)
            onData(allOf(
                    `is`(instanceOf(ScoutLaw::class.java)),
                    `is`(ScoutLaw(
                            i,
                            "Scout law $i test value.",
                            "Scout law $i description test value.",
                            "Scout law $i original description test value."
                    ))
            ))
                    .inAdapterView(withId(R.id.list_laws))
                    .check(matches(isDisplayed()))
    }

    @Test
    fun navigationDrawerShouldOpen() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START)))
                .perform(DrawerActions.open())

        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.START)))
    }

    @Test
    fun clickingOnQuizButtonShouldOpenQuizActivityWithChooserFragment() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        onView(withText(R.string.quiz_item))
                .perform(click())

        onView(withId(R.id.linear_quiz))
                .check(matches(isDisplayed()))
        onView(withId(R.id.card_chooser))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnAnyScoutLawShouldOpenDetailsActivityWithCorrespondingScoutLaw() {
        val i = Random().nextInt(9) + 1

        onView(withText("Scout law $i test value."))
                .perform(click())

        onView(withId(R.id.text_law))
                .check(matches(withText("Scout law $i test value.")))
    }
}