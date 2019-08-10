package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.List;

public class FolderRetriever extends ContentResolverRetriever {

    public FolderRetriever(ContentResolver contentResolver, String typeId) {
        super(contentResolver, typeId);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }

    @Override
    public String[] getProjection() {
        return new String[0];
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(@NonNull String id) {
        return null;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(@NonNull String query) {
        return null;
    }
}
