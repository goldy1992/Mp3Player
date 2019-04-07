package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class UserInterfaceUtils {
    private static final String LOG_TAG = "USER_INTERFACE_UTILS";

    public static void setClickable(View view, boolean value) {
        if (view != null) {
            view.setClickable(value);
            if (view instanceof ViewGroup) {
                ViewGroup vg = ((ViewGroup) view);
                for (int i = 0; i < vg.getChildCount(); i++) {
                    setClickable(vg.getChildAt(i), value);
                }
            }
        }
    }

    private static void saveState(Context context)
    {
        if (0 == 0) {
            return;
        }
        try {
            File fileToCache = new File(context.getCacheDir(), "mediaPlayerState");
            if (fileToCache.exists())
            {
                fileToCache.delete();
            }
            fileToCache.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(fileToCache);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(null);
            objectOut.close();
            fileOut.close();

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

}
