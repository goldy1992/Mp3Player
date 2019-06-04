package com.example.mike.mp3player;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;

public final class TestUtils {
    private TestUtils(){}

    public static final MediaItem createMediaItem() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder()
                .setMediaId("media_id").setTitle("title").build();
        return new MediaItem(mediaDescription, 0);
    }
}
