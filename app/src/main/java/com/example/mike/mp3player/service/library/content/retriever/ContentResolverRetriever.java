package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.ContentRequest;
import com.example.mike.mp3player.service.library.content.builder.MediaItemBuilder;

import java.util.List;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final MediaItemBuilder mediaItemBuilder;
    final String idPrefix;

    public ContentResolverRetriever(ContentResolver contentResolver, MediaItemBuilder mediaItemBuilder, String idPrefix) {
        super();
        this.contentResolver = contentResolver;
        this.mediaItemBuilder = mediaItemBuilder;
        this.idPrefix = idPrefix;
    }

    abstract Cursor performGetChildrenQuery(String id);
    abstract String[] getProjection();

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getSearchString());
        return mediaItemBuilder.build(cursor, request.getMediaIdPrefix());
    }


}
