package com.b4kancs.scoutlaws.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.quiz.multiple.clickCorrectAnswer
import com.b4kancs.scoutlaws.views.start.StartActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.b4kancs.scoutlaws.pressUp

/**
 * Created by hszilard on 17-May-18.
 */
@RunWith(AndroidJUnit4::class)
class BackNavigationTest {

    @get:Rule
    val quizActivityTestRule = ActivityTestRule<StartActivity>(StartActivity::class.java)

    @Test
    fun goToDetailsAndBack() {
        onView(withText("Scout law 1 test value."))
                .perform(click())
        /* We are now in Details activity */
        pressBack()

        onView(withId(R.id.list_laws))
                .check(matches(isDisplayed()))
    }

    @Test
    fun goToDetailsAndUp() {
        onView(withText("Scout law 1 test value."))
                .perform(click())
        /* We are now in Details activity */
        pressUp()

        onView(withId(R.id.list_laws))
                .check(matches(isDisplayed()))
    }

    @Test
    fun goToSettingsAndUp() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        onView(withText(R.string.settings_menu_item))
                .perform(click())
        /* We are now in Details activity */
        pressUp()

        onView(withId(R.id.list_laws))
                .check(matches(isDisplayed()))
    }

    @Test
    fun goToMultipleAndBack() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        onView(withText(R.string.quiz_menu_item))
                .perform(click())
        /* We are in the chooser fragment. Let's go to the multiple choice fragment */
        onView(withId(R.id.button_multiple))
                .perform(click())
        /* Let's go one question deep */
        clickCorrectAnswer()
        onView(withId(R.id.button_next))
                .perform(click())
        /* Now all the way back */
        pressBack()
        onView(withId(R.id.card_chooser))
                .check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.list_laws))
                .check(matches(isDisplayed()))
    }

    @Test
    fun goToMultipleAndUp() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
        onView(withText(R.string.quiz_menu_item))
                .perform(click())
        /* We are in the chooser fragment. Let's go to the multiple choice fragment */
        onView(withId(R.id.button_multiple))
                .perform(click())
        /* Let's go one question deep */
        clickCorrectAnswer()
        onView(withId(R.id.button_next))
                .perform(click())
        /* Now all the way back */
        pressUp()
        onView(withId(R.id.card_chooser))
                .check(matches(isDisplayed()))
        pressUp()
        onView(withId(R.id.list_laws))
                .check(matches(isDisplayed()))
    }
}