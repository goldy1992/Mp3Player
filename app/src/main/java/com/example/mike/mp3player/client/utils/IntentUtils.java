package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.activities.MediaPlayerActivityInjector;

/**
 * Utility classes for making intent objects for other classes and services
 */
public final class IntentUtils {
    /**
     * Creates an intent to go to the MediaPlayerActivity
     * @param context context
     * @return an Intent to go to the MediaPlayerActivity
     */
    public static Intent createGoToMediaPlayerActivity(@NonNull Context context) {
        return new Intent(context, MediaPlayerActivityInjector.class);
    }

}
