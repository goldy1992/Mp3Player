package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import java.util.List;
import java.util.Map;

public abstract class LibraryCollection extends LibraryCategory {

    Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> collection;

    public LibraryCollection(String id, String title, String description) {
        super(id, title, description);
    }
}
