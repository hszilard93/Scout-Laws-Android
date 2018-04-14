package com.b4kancs.scoutlaws.views.utils;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by hszilard on 14-Apr-18.
 */
public final class CommonUtils {
    private static final String LOG_TAG = CommonUtils.class.getSimpleName();

    private CommonUtils() {}

    public static void vibrate(@Nullable Context context, int duration) {
        Log.d(LOG_TAG, "Attempting to vibrate.");

        if (context != null) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null)
                vibrator.vibrate(duration);
        }
    }
}
