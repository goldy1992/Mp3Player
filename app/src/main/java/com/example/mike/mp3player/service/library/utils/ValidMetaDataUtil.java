package com.example.mike.mp3player.service.library.utils;

import android.support.v4.media.session.MediaSessionCompat;

import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;

public final class ValidMetaDataUtil {
    public static boolean validTitle(MediaSessionCompat.QueueItem queueItem) {
        return queueItem != null && queueItem.getDescription() != null
                && queueItem.getDescription().getTitle() != null
                && queueItem.getDescription().getTitle().toString() != null;
    }

    public static boolean validArtist(MediaSessionCompat.QueueItem queueItem) {
        return queueItem != null && queueItem.getDescription() != null
                && queueItem.getDescription().getExtras() != null
                && queueItem.getDescription().getExtras().get(STRING_METADATA_KEY_ARTIST) != null;
    }
}
