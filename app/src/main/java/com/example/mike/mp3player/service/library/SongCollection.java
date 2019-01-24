package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.Constants;

import java.util.ArrayList;
import java.util.List;

public class SongCollection extends LibraryCategory {

    public static final String ID = Constants.CATEGORY_SONGS_ID;
    public static final String TITLE = Constants.CATEGORY_SONGS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_SONGS_DESCRIPTION;

    public SongCollection() {
        super(ID, TITLE, DESCRIPTION);
    }

    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {
        this.keys = items;
    }

    public List<MediaBrowserCompat.MediaItem> getSongs() {
        return keys;
    }
}
