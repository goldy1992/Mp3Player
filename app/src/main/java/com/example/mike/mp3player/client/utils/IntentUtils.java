package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.activities.MediaPlayerActivityInjector;

import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;

/**
 * Utility classes for making intent objects for other classes and services
 */
public final class IntentUtils {
    /**
     * Creates an intent to go to the MediaPlayerActivity
     * @param context context
     * @param token the media session token
     * @return an Intent to go to the MediaPlayerActivity
     */
    public static Intent createGoToMediaPlayerActivity(@NonNull Context context, @NonNull MediaSessionCompat.Token token) {
        Intent intent = new Intent(context, MediaPlayerActivityInjector.class);
        intent.putExtra(MEDIA_SESSION, token);
        return intent;
    }

}
