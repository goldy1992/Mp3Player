package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final String childId;

    public ContentResolverRetriever(ContentResolver contentResolver, String childId, String parentId) {
        super(parentId);
        this.contentResolver = contentResolver;
        this.childId = childId;
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
