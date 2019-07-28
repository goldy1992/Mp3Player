package com.example.mike.mp3player.service.library.utils;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;


public final class ValidMetaDataUtil {
    public static boolean validTitle(MediaSessionCompat.QueueItem queueItem) {
        return notNullDescription(queueItem)
                && queueItem.getDescription().getTitle() != null
                && queueItem.getDescription().getTitle().toString() != null;
    }

    public static boolean validArtist(MediaSessionCompat.QueueItem queueItem) {
        return notNullDescription(queueItem)
                && queueItem.getDescription().getExtras() != null
                && queueItem.getDescription().getExtras().get(MediaMetadataCompat.METADATA_KEY_ARTIST) != null;
    }

    public static boolean validAlbumArtUri(MediaSessionCompat.QueueItem queueItem) {
        return notNullDescription(queueItem)
                && queueItem.getDescription().getExtras() != null
                && queueItem.getDescription().getExtras().get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI) != null;
    }

    public static boolean validMediaId(MediaSessionCompat.QueueItem queueItem) {
        return notNullDescription(queueItem)
            && queueItem.getDescription().getMediaId() != null;
    }

    private static boolean notNullDescription(MediaSessionCompat.QueueItem queueItem) {
        return queueItem != null && queueItem.getDescription() != null;
    }
}
