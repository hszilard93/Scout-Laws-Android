package com.b4kancs.scoutlaws

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.hamcrest.Matcher

/**
 * Created by hszilard on 15-May-18.
 */
internal fun countVisibleChildren(viewMatcher: Matcher<View>): Int {
    var numberOfVisibleChildren = 0
    onView(viewMatcher).perform(object : ViewAction {
        override fun getDescription(): String {
            return "Counting children."
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(ViewGroup::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            val viewGroup = view as ViewGroup
            for (i in 0 until viewGroup.childCount)
                if (viewGroup.getChildAt(i).isShown)
                    numberOfVisibleChildren++
        }
    })
    return numberOfVisibleChildren
}

/* Based on https://stackoverflow.com/a/23467629/4449809 */
internal fun extractText(matcher: Matcher<View>): String {
    lateinit var text: String
    onView(matcher).perform(object : ViewAction {
        override fun getDescription(): String {
            return "Extracting string from TextView"
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            text = (view as TextView).text.toString()
        }
    })
    return text
}

internal fun pressUp() =
        onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click())