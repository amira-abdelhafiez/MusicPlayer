package com.example.amira.musicplayer.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Amira on 2/2/2019.
 */

public class LayoutUtils {
    public static float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
    public static int calculateNoOfColumns(Context context , int scalingFactor) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
