package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

public abstract class ContentResolverRetriever implements ContentRetriever {

    final ContentResolver contentResolver;
    final String childId;


    public ContentResolverRetriever(ContentResolver contentResolver, String childId) {
        this.contentResolver = contentResolver;
        this.childId = childId;

    }

    public abstract MediaItemType getType();
    abstract Cursor getResults(String id);
    abstract String[] getProjection();


}
