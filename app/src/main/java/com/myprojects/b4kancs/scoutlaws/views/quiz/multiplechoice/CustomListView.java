package com.myprojects.b4kancs.scoutlaws.views.quiz.multiplechoice;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by hszilard on 28-Feb-18.
 */

public class CustomListView extends ListView {

    private int oldCount = 0;

    public CustomListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
////        if (getCount() != oldCount) {
////            int height = getChildAt(0).getHeight() + 10 ;
////            oldCount = getCount();
////            params = getLayoutParams();
////            params.height = getCount() * height;
////            setLayoutParams(params);
////        }
//
//        if (getCount() != oldCount) {
//            oldCount = getCount();
//            int maxHeight = 0;
//            int count = getChildCount();
//            for (int i = 0; i < count; i++) {
//                int height = getChildAt(i).getHeight() + 12;
//                maxHeight = maxHeight < height ? height : maxHeight;
//            }
//            params = getLayoutParams();
//            params.height = maxHeight * oldCount;
//            setLayoutParams(params);
//        }

        ListAdapter listAdapter = getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, this);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = totalHeight + (getDividerHeight() * (listAdapter.getCount() + getFooterViewsCount())) + getPaddingBottom() + 4;
        setLayoutParams(params);

        super.onDraw(canvas);
    }

}