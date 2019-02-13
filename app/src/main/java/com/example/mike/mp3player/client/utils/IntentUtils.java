package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.commons.library.LibraryId;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

/**
 * Utility classes for making intent objects for other classes and services
 */
public final class IntentUtils {

    public static Intent createMediaPlayerActivityMediaRequestIntent(Context context, MediaSessionCompat.Token token, String songId, LibraryId parentId) {
        Intent intent = createGoToMediaPlayerActivity(context, token);
        intent.putExtra(MEDIA_ID, songId);
        intent.putExtra(PARENT_ID, parentId);
        return intent;
    }

    public static Intent createGoToMediaPlayerActivity(Context context, MediaSessionCompat.Token token) {
        Intent intent = new Intent(context, MediaPlayerActivity.class);
        intent.putExtra(MEDIA_SESSION, token);
        return intent;
    }

}
