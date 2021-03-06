package com.b4kancs.scoutlaws.views.utils

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.b4kancs.scoutlaws.R

/**
 * Created by hszilard on 22-Feb-18.
 */

private const val PACKAGE_NAME = "com.b4kancs.scoutlaws"

/* Set the background color based on the scout law's number */
@BindingAdapter("background_number")
fun setBackgroundColorBinding(view: View, i: Int) {
    if (isPastelEnabled(view.context))
        view.setBackgroundColor(getColorCode(view.resources, i, "_alternative"))
    else
        view.setBackgroundColor(getColorCode(view.resources, i, ""))
}

/* Set the text color based on the scout law's number */
@BindingAdapter("textColor_number")
fun setTextColorBinding(textView: TextView, i: Int) {
    if (isPastelEnabled(textView.context))
        giveTextShadow(textView, i)
    textView.setTextColor(getColorCode(textView.resources, i, "_text"))
}

@BindingAdapter("animateLayoutChanges")
fun animateLayoutChangesBinding(viewGroup: ViewGroup, context: Context) {
    if (areAnimationsEnabled(context))
        viewGroup.layoutTransition = LayoutTransition()
    else
        viewGroup.layoutTransition = null
}

@BindingAdapter("colorPrimaryOrLight")
fun setPrimaryDefaultOrLightBackground(view: View, doesNothing: Boolean) {
    view.setBackgroundColor(getPrimaryOrPrimaryLight(view.context))
}

@BindingAdapter("textShadow")
fun determineTextShadow(textView: TextView, doesNothing: Boolean) {
    if (isPastelEnabled(textView.context))
        giveTextShadow(textView)
}

private fun giveTextShadow(textView: TextView, i: Int = 0) {
    textView.resources.apply {
        if (i != 7)
            textView.setShadowLayer(1.2f, 0.3f, 0.3f, getColor(R.color.black))
    }
}

fun getPrimaryOrPrimaryLight(context: Context): Int {
    context.resources.apply {
        return if (isPastelEnabled(context))
            getColor(R.color.colorGreenAlternative)
        else
            getColor(R.color.colorPrimary)
    }
}

private fun getColorCode(resources: Resources, i: Int, suffix: String): Int {
    return resources.getColor(resources.getIdentifier("color_item$i$suffix", "color", PACKAGE_NAME))
}