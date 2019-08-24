package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.service.library.utils.IdGenerator;

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

    abstract Cursor getResults(String id);
    abstract String[] getProjection();
    abstract MediaBrowserCompat.MediaItem buildMediaItem(Cursor cursor, @Nullable String parentId);

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(String fullId, String searchId) {
        Cursor cursor = getResults(searchId);
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(this);
        while (cursor.moveToNext()) {
            MediaBrowserCompat.MediaItem mediaItem = buildMediaItem(cursor, fullId);
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
