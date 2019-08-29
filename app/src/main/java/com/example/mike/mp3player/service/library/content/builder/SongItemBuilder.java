package com.example.mike.mp3player.service.library.content.builder;

import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.List;

public class SongItemBuilder extends MediaItemBuilder {

    @Override
    public List<MediaBrowserCompat.MediaItem> build(Cursor cursor, String mediaIdPrefix) {
        return null;
    }

    @Override
    public MediaItemType getType() {
        return null;
    }

    @Override
    public int compare(MediaBrowserCompat.MediaItem o1, MediaBrowserCompat.MediaItem o2) {
        return 0;
    }
}
