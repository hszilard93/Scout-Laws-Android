package com.b4kancs.scoutlaws.views.quiz.pickandchoose;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.b4kancs.scoutlaws.R;
import com.b4kancs.scoutlaws.views.utils.CommonUtils;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;

/**
 * Created by hszilard on 14-Apr-18.
 */
public final class PickAndChooseBindings {

    private PickAndChooseBindings() {
    }

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
        MyAnimator animator = new MyAnimator(view, targetWidth, MyAnimator.Type.WIDTH, 150);
        view.startAnimation(animator);
    }

    private static void resizeViewWithAnimation(View view, int targetWidth) {

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
                TextView optionView = (TextView) inflater.inflate(R.layout.text_view_pick_choose_option, layout, false);
                optionView.setText(item);
                optionView.setOnTouchListener(new OptionTouchListener(options));
                layout.addView(optionView);
            }
        }
    }

    private static class OptionTouchListener implements View.OnTouchListener {
        ArrayList<String> options;

        public OptionTouchListener(ArrayList<String> options) {
            this.options = options;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            /* Check if targetView is eligible for drag & drop */
            if (view.getId() != R.id.option_textView)
                return false;

            String text = ((TextView) view).getText().toString();
            ClipData clipData = ClipData.newPlainText(null, text);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, view, 0);

            // This change will be reflected in the optionsFlowLayout
            options.remove(text);
            // Provide feedback to user
            CommonUtils.vibrate(view.getContext(), 50);

            return true;
        }
    }

    /* Riley MacDonald's solution from https://rileymacdonald.ca/2015/01/15/android-animating-views-using-width-or-height/. Thanks Riley! */
    static class MyAnimator extends Animation {
        enum Type {WIDTH, HEIGHT}

        private final int fromDimension;
        private int toDimension;
        private Type type;
        private View targetView;

        public MyAnimator(View view, int toDimension, Type type, long duration) {
            this.targetView = view;
            this.fromDimension = type == Type.WIDTH ? view.getLayoutParams().width : view.getLayoutParams().height;
            this.toDimension = toDimension;
            this.type = type;

            this.setDuration(duration);
        }

        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            // Used to apply the animation to the targetView
            final int curPos = fromDimension + (int) ((toDimension - fromDimension) * interpolatedTime);

            // Animate given the height or width
            switch (type) {
                case WIDTH:
                    targetView.getLayoutParams().width = curPos;
                    break;
                case HEIGHT:
                    targetView.getLayoutParams().height = curPos;
                    break;
            }

            // Ensure the targetView is measured appropriately
            targetView.requestLayout();
        }
    }

}
