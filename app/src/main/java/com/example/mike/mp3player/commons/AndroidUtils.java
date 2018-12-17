package com.example.mike.mp3player.commons;

import android.os.Build;

public final class AndroidUtils {


    public static boolean isAndroidOreoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static int getAndroidApiVersion() {
        return Build.VERSION.SDK_INT;
    }
}
