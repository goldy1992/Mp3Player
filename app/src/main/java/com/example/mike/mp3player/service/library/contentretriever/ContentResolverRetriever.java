package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final String idPrefix;

    public ContentResolverRetriever(ContentResolver contentResolver, String idPrefix) {
        super();
        this.contentResolver = contentResolver;
        this.idPrefix = idPrefix;
    }

    abstract Cursor getResults(String id);
    abstract String[] getProjection();
    abstract MediaBrowserCompat.MediaItem buildMediaItem(Cursor cursor);

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(String id) {
        Cursor cursor = getResults(id);
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(this);
        while (cursor.moveToNext()) {
            MediaBrowserCompat.MediaItem mediaItem = buildMediaItem(cursor);
            if (null != mediaItem) {
                listToReturn.add(mediaItem);
            }
        }

        return new ArrayList<>(listToReturn);
    }


}
