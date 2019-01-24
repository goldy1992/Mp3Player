package com.example.mike.mp3player.service.library.utils;

import android.os.Bundle;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public final class MediaItemUtils {

    public static boolean hasExtras(MediaItem item) {
        return item != null && item.getDescription() != null && item.getDescription().getExtras() != null;
    }

    public static boolean hasMediaId(MediaItem item) {
        return item != null && item.getDescription() != null && item.getDescription().getMediaId() != null;
    }

    public static Bundle getExtras(MediaItem item) {
        if (!hasExtras(item)) {
            return null;
        }
        return item.getDescription().getExtras();
    }

    public static String getMediaId(MediaItem item) {
        if (!hasMediaId(item)) {
            return null;
        }
        return item.getDescription().getMediaId();
    }
}
