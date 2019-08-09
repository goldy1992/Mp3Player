package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.support.v4.media.MediaBrowserCompat;

import java.util.List;

public abstract class ContentRetriever {

    private final ContentResolver contentResolver;

    public ContentRetriever(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public abstract MediaItemType getType();
    public abstract String[] getProjection();
    public abstract List<MediaBrowserCompat.MediaItem> getChildren(String id);
    public abstract List<MediaBrowserCompat.MediaItem> search(String query);
}
