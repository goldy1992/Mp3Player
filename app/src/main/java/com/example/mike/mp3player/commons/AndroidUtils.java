package com.example.mike.mp3player.commons;

import android.os.Build;

import com.example.mike.mp3player.BuildConfig;

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

    public static boolean isNougatOrLower() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.N;
    }

    public static int getAndroidApiVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getProductFlavor () { return BuildConfig.FLAVOR; }

}
