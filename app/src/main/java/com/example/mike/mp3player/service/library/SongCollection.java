package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.Constants;

import java.util.List;

public class SongCollection extends LibraryCollection {

    public static final String ID = Constants.CATEGORY_SONGS_ID;
    public static final String TITLE = Constants.CATEGORY_SONGS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_SONGS_DESCRIPTION;

    public SongCollection() {
        super(Category.SONGS, ID, TITLE, DESCRIPTION);
    }

    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {
        this.getKeys().addAll(items);
    }

    public List<MediaBrowserCompat.MediaItem> getSongs() {
        return getKeys();
    }
}
