package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.support.v4.media.MediaBrowserCompat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.Comparator;
import java.util.List;

public abstract class ContentRetriever implements Comparator<MediaBrowserCompat.MediaItem> {

    final String parentId;

    public ContentRetriever(String parentId) {
        this.parentId = parentId;
    }

    public abstract List<MediaBrowserCompat.MediaItem> getChildren(@Nullable String id);
    public abstract List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
    public abstract MediaItemType getType();
    public abstract MediaItemType getParentType();
}
