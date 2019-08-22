package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.support.v4.media.MediaBrowserCompat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.commons.MediaItemLibraryInfo;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemTypeInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class ContentRetriever implements Comparator<MediaBrowserCompat.MediaItem> {

    private final Map<MediaItemTypeInfo, MediaItemLibraryInfo> childrenInfos;

    public ContentRetriever(Map<MediaItemTypeInfo, MediaItemLibraryInfo> childrenInfos) {
        this.childrenInfos = childrenInfos;
    }

    public abstract List<MediaBrowserCompat.MediaItem> getChildren(@Nullable String id);
    public abstract List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
    public abstract MediaItemType getType();
    public abstract MediaItemType getParentType();
}
