package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;

import java.util.List;

public class RootLibraryCollection extends LibraryCollection {

    public static final String ID = Constants.CATEGORY_SONGS_ID;
    public static final String TITLE = Constants.CATEGORY_SONGS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_SONGS_DESCRIPTION;

    public RootLibraryCollection() {
        super(ID, TITLE, DESCRIPTION);
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(String id) {
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
