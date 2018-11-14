package com.b4kancs.scoutlaws.views.details

import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.b4kancs.scoutlaws.R
import com.b4kancs.scoutlaws.views.details.DetailsActivity.SCOUT_LAW_NUMBER_KEY
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by hszilard on 13-May-18.
 */
@RunWith(AndroidJUnit4::class)
class DetailsActivityScreenTest {

    @get:Rule
    val detailsActivityTestRule = object : ActivityTestRule<DetailsActivity>(DetailsActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val intent = Intent()
            intent.putExtra(SCOUT_LAW_NUMBER_KEY, 1)    // Show the first scout law
            return intent
        }
    }

    @Test
    fun correctViewsShouldBeDisplayedAfterStarted() {
        onView(withId(R.id.text_number))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_modern))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_old))
                .check(matches(not(isDisplayed())))
        onView(withId(R.id.text_source))
                .check(matches(not(isDisplayed())))
    }

    @Test
    fun correctViewShouldBeDisplayedWhenOldSelected() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())

        onView(withText(R.string.original))    // Id matcher wouldn't work
                .perform(click())

        onView(withId(R.id.text_number))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_modern))
                .check(matches(not(isDisplayed())))
        onView(withId(R.id.text_old))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_source))
                .check(matches(isDisplayed()))
    }

    @Test
    fun correctViewShouldBeDisplayedWhenOldSelectedThenModernSelected() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.original))    // Id matcher wouldn't work
                .perform(click())
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.contemporary))    // Id matcher wouldn't work
                .perform(click())

        onView(withId(R.id.text_number))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_modern))
                .check(matches(isDisplayed()))
        onView(withId(R.id.text_old))
                .check(matches(not(isDisplayed())))
        onView(withId(R.id.text_source))
                .check(matches(not(isDisplayed())))
    }
}