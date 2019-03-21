package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.Range;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.List;
import java.util.Set;

public class SongCollection extends LibraryCollection {

    public static final String ID = Category.SONGS.name();
    public static final String TITLE = Constants.CATEGORY_SONGS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_SONGS_DESCRIPTION;

    public SongCollection() {
        super(ID, TITLE, DESCRIPTION, ComparatorUtils.compareMediaItemsByTitle, null);
    }

    @Override
    public Set<MediaBrowserCompat.MediaItem> getChildren(LibraryId id, Range range) {
        return getKeys(range);
    }

    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {
        if (items != null) {
            this.getKeys().addAll(items);
        }
    }

    @Override
    public Category getRootId() {
        return Category.SONGS;
    }

    public Set<MediaBrowserCompat.MediaItem> getSongs() {
        return getKeys();
    }

}
