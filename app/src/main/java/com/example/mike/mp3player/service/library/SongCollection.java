package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.library.db.AppDatabase;
import com.example.mike.mp3player.service.library.db.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SongCollection extends LibraryCollection {

    public static final String ID = Category.SONGS.name();
    public static final String TITLE = Constants.CATEGORY_SONGS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_SONGS_DESCRIPTION;

    public SongCollection() {
        super(ID, TITLE, DESCRIPTION, ComparatorUtils.compareMediaItemsByTitle, null);
    }

    @Override
    public TreeSet<MediaBrowserCompat.MediaItem> getChildren(LibraryObject id) {
        // never used for songs collection as a song cannot have a child
        return null;
    }

    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {
        AppDatabase database = null;
        List<Song> songs = new ArrayList<>();
        for (MediaBrowserCompat.MediaItem mediaItem : items) {
            Song song = new Song();

       //     song.mediaItem = mediaItem;
            songs.add(song);
        }
        Song[] mediaItemsArray = new Song[items.size()];
        mediaItemsArray = songs.toArray(mediaItemsArray);
        database.songDao().insertAll(mediaItemsArray);
        if (items != null) {
            this.getKeys().addAll(items);
        }
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(String query) {
        return null;
    }

    @Override
    public Category getRootId() {
        return Category.SONGS;
    }

    public TreeSet<MediaBrowserCompat.MediaItem> getSongs() {
        return getKeys();
    }
}
