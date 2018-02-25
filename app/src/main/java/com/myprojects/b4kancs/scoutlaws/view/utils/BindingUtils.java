package com.myprojects.b4kancs.scoutlaws.view.utils;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.myprojects.b4kancs.scoutlaws.R;
import com.myprojects.b4kancs.scoutlaws.data.model.ScoutLaw;

/**
 * Created by hszilard on 22-Feb-18.
 */

public class BindingUtils {
    private static final String PACKAGE_NAME = "com.myprojects.b4kancs.scoutlaws";

    /* Set the background color based on the scoutlaw's number */
    @BindingAdapter("app:background")
    public static void setBackgroundColor(@NonNull View view, int i) {
        view.setBackgroundColor(getColorCode(view.getResources(), i, ""));
    }

    /* Set the text color based on the scoutlaw's number */
    @BindingAdapter("app:textColor")
    public static void setTextColor(@NonNull TextView textView, int i) {
        textView.setTextColor(getColorCode(textView.getResources(), i, "_text"));
    }

    /* Set the scoutlaw's description based on a flag */
    @BindingAdapter({"scoutLaw", "isModern"})
    public static void setScoutLawDesc(@NonNull TextView textView, ScoutLaw scoutLaw, boolean isModern) {
        textView.setText(isModern ? scoutLaw.description : scoutLaw.originalDescription);
    }

    /* Set the description source based on a flag */
    @BindingAdapter("app:descSource")
    public static void setDescriptionSource(@NonNull TextView textView, boolean isModern) {
        Resources resources = textView.getResources();
        textView.setText(isModern? resources.getString(R.string.source_modern) : resources.getString(R.string.source_orig));
    }

    private static int getColorCode(Resources resources, int i, String suffix) {
        return resources.getColor(resources.getIdentifier("color_item" + i + suffix, "color", PACKAGE_NAME));
    }

}
