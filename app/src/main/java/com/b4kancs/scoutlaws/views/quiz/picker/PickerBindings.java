package com.b4kancs.scoutlaws.views.quiz.picker;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.Resources;
import androidx.databinding.BindingAdapter;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;

import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.vibrate;

/**
 * Created by hszilard on 14-Apr-18.
 */
public final class PickerBindings {

    private PickerBindings() { }

    @BindingAdapter("fillTextView_adapter")
    public static void AdaptFillTextView(@NonNull TextView view, String text) {
        view.setText(text);

        Resources resources = view.getResources();
        int targetWidth;
        if (text == null) {
            view.setBackground(resources.getDrawable(R.drawable.rounded_empty_background));
            // set width to 96dp
            float scale = resources.getDisplayMetrics().density;
            targetWidth = (int) (96 * scale + 0.5f);
        } else {
            view.setBackground(resources.getDrawable(R.drawable.rounded_option_background));
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            targetWidth = view.getMeasuredWidth();
        }
        view.getLayoutParams().width = targetWidth;
        view.requestLayout();
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("optionsFlow_adapter")
    public static void AdaptOptionsFlowLayout(@NonNull FlowLayout layout, ArrayList<String> options) {
        // Remove views from the layout whose content are not in options
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView textView = ((TextView) layout.getChildAt(i));
            if (!options.contains(textView.getText().toString()))
                layout.removeViewAt(i);
        }

        // Then get the items that are displayed
        ArrayList<String> displayed = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++)
            displayed.add(((TextView) layout.getChildAt(i)).getText().toString());

        // Check them against all the options and display those missing
        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        for (String item : options) {
            if (!displayed.contains(item)) {
                TextView optionView = (TextView) inflater.inflate(R.layout.text_view_picker_option, layout, false);
                optionView.setText(item);
                optionView.setOnTouchListener(new OptionTouchListener(options));
                layout.addView(optionView);
            }
        }
    }

    private static class OptionTouchListener implements View.OnTouchListener {
        ArrayList<String> options;

        OptionTouchListener(ArrayList<String> options) {
            this.options = options;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            /* Check if targetView is eligible for drag & drop */
            if (view.getId() != R.id.text_option)
                return false;

            String text = ((TextView) view).getText().toString();
            ClipData clipData = ClipData.newPlainText(null, text);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, view, 0);

            // This change will be reflected in the optionsFlowLayout
            options.remove(text);
            // Provide feedback to user
            vibrate(view.getContext(), 50);

            return true;
        }
    }
}
