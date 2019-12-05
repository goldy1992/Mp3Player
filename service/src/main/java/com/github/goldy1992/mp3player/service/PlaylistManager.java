package com.github.goldy1992.mp3player.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

@Singleton
public class PlaylistManager {

    private static final int START_OF_PLAYLIST = 0;
    private static final int EMPTY_PLAYLIST_INDEX = -1;
    private int queueIndex = EMPTY_PLAYLIST_INDEX;
    private final List<MediaItem> playlist;


    @Inject
    public PlaylistManager(@Named("starting_playlist")List<MediaItem> items) {
        this.playlist = items;
        this.queueIndex = START_OF_PLAYLIST;
    }

    public synchronized boolean createNewPlaylist(List<MediaItem> newList) {
        playlist.clear();
        boolean result = playlist.addAll(newList);
        queueIndex = playlist.isEmpty() ?  EMPTY_PLAYLIST_INDEX : START_OF_PLAYLIST;
        return result;
    }

    public synchronized MediaItem getItemAtIndex(int index) {
        if (validQueueIndex(index)) {
            return playlist.get(index);
        }
        return null;
    }


    public synchronized MediaItem getCurrentItem() {
        if (validQueueIndex(queueIndex)) {
            return playlist.get(queueIndex);
        }
        return  null;
    }

    private synchronized boolean validQueueIndex(int newQueueIndex) {
        return newQueueIndex < playlist.size() && newQueueIndex >= 0;
    }

    public List<MediaItem> getPlaylist() {
        return playlist;
    }
}
