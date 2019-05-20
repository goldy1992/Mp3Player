package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Unused at the moment
 */
public final class UserInterfaceUtils {
    private static final String LOG_TAG = "USER_INTERFACE_UTILS";

    /**
     * currently unused, util method for the future to make items clickable
     * @param view the view to be set/unset clickable
     * @param value true if to be set clickable, false otherwise
     */
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

    /**
     * currently unused. Could possibly use in the future
     * @param context context
     */
    private static void saveState(Context context)
    {
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        try {
            File fileToCache = new File(context.getCacheDir(), "mediaPlayerState");
            if (fileToCache.exists())
            {
                fileToCache.delete();
            }
            fileToCache.createNewFile();
            fileOut = new FileOutputStream(fileToCache);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(null);


        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            try {
                if (null != objectOut) {
                    objectOut.close();
                }
                if (null != fileOut) {
                    fileOut.close();
                }
            } catch (IOException ex) {
                Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex));
            }
        } // finally
    }

}
