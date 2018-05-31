package com.b4kancs.scoutlaws.views.utils

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.Resources
import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by hszilard on 22-Feb-18.
 */

private const val PACKAGE_NAME = "com.b4kancs.scoutlaws"

/* Set the background color based on the scout law's number */
@BindingAdapter("background_number")
fun setBackgroundColorBinding(view: View, i: Int) {
    view.setBackgroundColor(getColorCode(view.resources, i, ""))
}

/* Set the text color based on the scoutlaw's number */
@BindingAdapter("textColor_number")
fun setTextColorBinding(textView: TextView, i: Int) {
    textView.setTextColor(getColorCode(textView.resources, i, "_text"))
}

@BindingAdapter("animateLayoutChanges")
fun animateLayoutChangesBinding(viewGroup: ViewGroup, context: Context) {
    if (areAnimationsEnabled(context))
        viewGroup.layoutTransition = LayoutTransition()
    else
        viewGroup.layoutTransition = null
}

private fun getColorCode(resources: Resources, i: Int, suffix: String): Int {
    return resources.getColor(resources.getIdentifier("color_item$i$suffix", "color", PACKAGE_NAME))
}
