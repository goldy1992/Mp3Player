package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.List;

public class RootLibraryCollection extends LibraryCollection {

    public static final String ID = Constants.CATEGORY_ROOT_ID;

    public RootLibraryCollection() {
        super(ID, ID, ID);
    }

    @Deprecated
    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(String id) {
        return null;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(LibraryId id) {
        return null;
    }
    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {
        this.getKeys().addAll(items);
    }

    @Override
    public Category getRootId() {
        return Category.ROOT;
    }
}
