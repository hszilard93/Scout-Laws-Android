package com.myprojects.b4kancs.scoutlaws.view.utils;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hszilard on 22-Feb-18.
 */

public class BindingUtils {
    private static final String PACKAGE_NAME = "com.myprojects.b4kancs.scoutlaws";

    @BindingAdapter("app:background")
    public static void setBackgroundColor(@NonNull View view, int i) {
        Resources resources = view.getResources();
        int colorCode = resources.getColor(resources.getIdentifier("color_item" + i, "color", PACKAGE_NAME));
        view.setBackgroundColor(colorCode);
    }

    @BindingAdapter("app:textColor")
    public static void setTextColor(@NonNull TextView textView, int i) {
        Resources resources = textView.getResources();
        int colorCode = resources.getColor(resources.getIdentifier("color_item" + i + "_text", "color", PACKAGE_NAME));
        textView.setTextColor(colorCode);
    }

}
