package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.service.library.content.builder.MediaItemCreator;

import java.util.List;

public abstract class ContentResolverSearcher extends ContentSearcher {
    final ContentResolver contentResolver;
    final MediaItemCreator mediaItemCreator;
    final String idPrefix;

    public ContentResolverSearcher(ContentResolver contentResolver,
                                   MediaItemCreator mediaItemCreator,
                                   String idPrefix) {
        this.contentResolver = contentResolver;
        this.mediaItemCreator = mediaItemCreator;
        this.idPrefix = idPrefix;
    }

    public List<MediaBrowserCompat.MediaItem> search(@NonNull String query) {
        Cursor cursor = performSearchQuery(query);
        return mediaItemCreator.build(cursor, idPrefix);
    }

    abstract String[] getProjection();
    abstract Cursor performSearchQuery(String query);
}