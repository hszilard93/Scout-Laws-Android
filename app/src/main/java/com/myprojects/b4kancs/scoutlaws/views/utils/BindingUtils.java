package com.myprojects.b4kancs.scoutlaws.views.utils;

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
    @BindingAdapter("background_number")
    public static void setBackgroundColor(@NonNull View view, int i) {
        view.setBackgroundColor(getColorCode(view.getResources(), i, ""));
    }

    /* Set the text color based on the scoutlaw's number */
    @BindingAdapter("textColor_number")
    public static void setTextColor(@NonNull TextView textView, int i) {
        textView.setTextColor(getColorCode(textView.getResources(), i, "_text"));
    }

    /* Set the scoutlaw's description based on a flag */
    @BindingAdapter({"descText_scoutLaw", "descText_isModern"})
    public static void setScoutLawDesc(@NonNull TextView textView, ScoutLaw scoutLaw, boolean isModern) {
        textView.setText(isModern ? scoutLaw.description : scoutLaw.originalDescription);
    }

    /* Set the description source based on a flag */
    @BindingAdapter("descSourceText_isModern")
    public static void setDescriptionSource(@NonNull TextView textView, boolean isModern) {
        Resources resources = textView.getResources();
        textView.setText(isModern? resources.getString(R.string.source_modern) : resources.getString(R.string.source_orig));
    }

    /* Set the text of a Multiple choice question (e.g. "Which is the 1st law of the scouts?") */
    @BindingAdapter("multipleQuestionText_number")
    public static void setMultipleQuestionText(@NonNull TextView textView, int i) {
        Resources resources = textView.getResources();
        String questionText = String.format(resources.getString(R.string.multiple_quiz_question), i);
        textView.setText(questionText);
    }

    private static int getColorCode(Resources resources, int i, String suffix) {
        return resources.getColor(resources.getIdentifier("color_item" + i + suffix, "color", PACKAGE_NAME));
    }

}
