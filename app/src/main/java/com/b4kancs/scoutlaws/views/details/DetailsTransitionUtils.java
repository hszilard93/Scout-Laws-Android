package com.b4kancs.scoutlaws.views.details;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b4kancs.scoutlaws.databinding.ActivityDetailsBinding;

import static com.b4kancs.scoutlaws.views.utils.CommonUtilsKt.areAnimationsEnabled;

/**
 * Created by hszilard on 10-Nov-18.
 */
public class DetailsTransitionUtils {
    /* Changes out the description in DetailsActivity based on state */
    @BindingAdapter("state_binding")
    public static void stateBindingAdapter(@NonNull ViewGroup layout, DetailsActivityViewModel.State state) {
        ActivityDetailsBinding binding = DataBindingUtil.findBinding(layout);
        TextView modern = binding.textModern;
        LinearLayout old = binding.linearOld;

        if (modern.getVisibility() == View.VISIBLE && state == DetailsActivityViewModel.State.OLD) {
            // change out the views
            if (areAnimationsEnabled(layout.getContext()))
                executeViewTransitionsToLeft(modern, old);
            else {
                modern.setVisibility(View.GONE);
                old.setVisibility(View.VISIBLE);
            }
        } else if (old.getVisibility() == View.VISIBLE && state == DetailsActivityViewModel.State.MODERN) {
            // change out the views
            if (areAnimationsEnabled(layout.getContext()))
                executeViewTransitionsToRight(old, modern);
            else {
                old.setVisibility(View.GONE);
                modern.setVisibility(View.VISIBLE);
            }
        }
    }

    private static void executeViewTransitionsToLeft(View outView, View inView) {
        int screenWidth = getScreenWidth(outView.getContext());
        Animator outAnimator = ObjectAnimator.ofFloat(outView, "translationX", screenWidth * -1);
        outAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                outView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        inView.setTranslationX(screenWidth);
        inView.setVisibility(View.VISIBLE);
        Animator inAnimator = ObjectAnimator.ofFloat(inView, "translationX", 0);
        outAnimator.start();
        inAnimator.start();
    }

    private static void executeViewTransitionsToRight(View outView, View inView) {
        int screenWidth = getScreenWidth(outView.getContext());

        Animator outAnimator = ObjectAnimator.ofFloat(outView, "translationX", screenWidth);
        outAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                outView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        inView.setTranslationX(screenWidth * -1);
        inView.setVisibility(View.VISIBLE);
        Animator inAnimator = ObjectAnimator.ofFloat(inView, "translationX", 0);
        outAnimator.start();
        inAnimator.start();
    }

    private static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
