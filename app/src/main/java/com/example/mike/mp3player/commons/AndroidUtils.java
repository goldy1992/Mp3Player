package com.example.mike.mp3player.commons;

import android.os.Build;

public final class AndroidUtils {


    public static boolean isAndroidOreoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean isAndroidMarshmallowOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isMashmallowOrLower() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.M;
    }

    public static int getAndroidApiVersion() {
        return Build.VERSION.SDK_INT;
    }
}
