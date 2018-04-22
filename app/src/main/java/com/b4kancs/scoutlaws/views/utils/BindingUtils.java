package com.b4kancs.scoutlaws.views.utils;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hszilard on 22-Feb-18.
 */

public final class BindingUtils {
    private static final String PACKAGE_NAME = "com.b4kancs.scoutlaws";

    private BindingUtils() {}

    /* Set the background color based on the scout law's number */
    @BindingAdapter("background_number")
    public static void setBackgroundColor(@NonNull View view, int i) {
        view.setBackgroundColor(getColorCode(view.getResources(), i, ""));
    }

    /* Set the text color based on the scoutlaw's number */
    @BindingAdapter("textColor_number")
    public static void setTextColor(@NonNull TextView textView, int i) {
        textView.setTextColor(getColorCode(textView.getResources(), i, "_text"));
    }

    private static int getColorCode(Resources resources, int i, String suffix) {
        return resources.getColor(resources.getIdentifier("color_item" + i + suffix, "color", PACKAGE_NAME));
    }

}
