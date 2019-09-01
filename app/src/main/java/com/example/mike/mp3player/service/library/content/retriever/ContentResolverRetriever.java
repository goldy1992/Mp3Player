package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.content.request.ContentRequest;
import com.example.mike.mp3player.service.library.content.builder.MediaItemCreator;

import java.util.List;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final MediaItemCreator mediaItemCreator;

    public ContentResolverRetriever(ContentResolver contentResolver, MediaItemCreator mediaItemCreator) {
        super();
        this.contentResolver = contentResolver;
        this.mediaItemCreator = mediaItemCreator;
    }

    abstract Cursor performGetChildrenQuery(String id);
    abstract String[] getProjection();

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getQueryString());
        return mediaItemCreator.build(cursor, request.getMediaIdPrefix());
    }
}
