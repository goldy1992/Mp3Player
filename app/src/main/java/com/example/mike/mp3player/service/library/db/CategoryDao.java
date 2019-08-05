package com.example.mike.mp3player.service.library.db;

import android.support.v4.media.MediaBrowserCompat;

import java.util.TreeSet;

public interface CategoryDao {

    public abstract TreeSet<MediaBrowserCompat.MediaItem> getChildren();
}
