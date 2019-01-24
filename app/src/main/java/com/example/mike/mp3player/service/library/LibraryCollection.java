package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import java.util.List;
import java.util.Map;

public abstract class LibraryCollection extends LibraryCategory {

    protected Map<String, List<MediaBrowserCompat.MediaItem>> collection;

    public abstract List<MediaBrowserCompat.MediaItem> getChildren(String id);

    public LibraryCollection(String id, String title, String description) {
        super(id, title, description);
    }
}
