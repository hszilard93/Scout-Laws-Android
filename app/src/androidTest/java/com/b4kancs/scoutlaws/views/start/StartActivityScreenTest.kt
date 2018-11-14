package com.b4kancs.scoutlaws.views.start

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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
        onView(withText(R.string.quiz_menu_item))
                .perform(click())

        onView(withId(R.id.linear_quiz))
                .check(matches(isDisplayed()))
        onView(withId(R.id.card_chooser))
                .check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnSettingsButtonShouldOpenSettingsActivity() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        onView(withText(R.string.settings_menu_item))
                .perform(click())

        onView(withId(R.id.frame_preferences_content))
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