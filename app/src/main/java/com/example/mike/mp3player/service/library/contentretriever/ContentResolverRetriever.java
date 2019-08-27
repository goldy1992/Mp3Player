package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.ContentRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Nullable;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final String idPrefix;

    public ContentResolverRetriever(ContentResolver contentResolver, String idPrefix) {
        super();
        this.contentResolver = contentResolver;
        this.idPrefix = idPrefix;
    }

    abstract Cursor performGetChildrenQuery(String id);
    abstract String[] getProjection();
    abstract MediaItem buildMediaItem(Cursor cursor, @Nullable String parentId);

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getSearchString());
        TreeSet<MediaItem> listToReturn = new TreeSet<>(this);
        while (cursor.moveToNext()) {
            MediaItem mediaItem = buildMediaItem(cursor, request.getFullId());
            if (null != mediaItem) {
                listToReturn.add(mediaItem);
            }
        }
        return new ArrayList<>(listToReturn);
    }

    protected String buildMediaId(String customIdPrefix, String childItemId) {
        return super.buildMediaId(this.idPrefix == null ? customIdPrefix : this.idPrefix, childItemId);
    }

}
