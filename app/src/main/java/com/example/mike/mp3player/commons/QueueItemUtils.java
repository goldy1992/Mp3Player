package com.example.mike.mp3player.commons;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;

import static android.support.v4.media.session.MediaSessionCompat.QueueItem;


public final class QueueItemUtils {

    public static boolean hasExtras(QueueItem item) {
        return item != null && item.getDescription().getExtras() != null;
    }

    public static boolean hasMediaId(QueueItem item) {
        return item != null && item.getDescription().getMediaId() != null;
    }

    public static boolean hasTitle(QueueItem item) {
        return item != null && item.getDescription().getTitle() != null;
    }

    public static boolean hasArtist(QueueItem item) {
        if (hasExtras(item)) {
            return getExtra(MediaMetadataCompat.METADATA_KEY_ARTIST, item) != null;
        }
        return false;
    }

    public static boolean hasDescription(QueueItem item) {
        return item != null && item.getDescription().getDescription() != null;
    }

    public static Bundle getExtras(QueueItem item) {
        if (!hasExtras(item)) {
            return null;
        }
        return item.getDescription().getExtras();
    }

    public static Object getExtra(String key, QueueItem item) {
        if (!hasExtras(item)) {
            return null;
        }
        return item.getDescription().getExtras().get(key);
    }

    public static String getMediaId(QueueItem item) {
        if (!hasMediaId(item)) {
            return null;
        }
        return item.getDescription().getMediaId();
    }

    public static String getTitle(QueueItem i) {
        if (hasTitle(i)) {
            return i.getDescription().getTitle().toString();
        }
        return null;
    }



    public static String getDescription(QueueItem item) {
        if (hasDescription(item)) {
            return item.getDescription().getDescription().toString();
        }
        return null;
    }
}
