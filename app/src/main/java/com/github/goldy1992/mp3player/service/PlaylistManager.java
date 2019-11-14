package com.github.goldy1992.mp3player.service;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class PlaylistManager {

    private static final int START_OF_PLAYLIST = 0;
    private static final int EMPTY_PLAYLIST_INDEX = -1;
    private int queueIndex = EMPTY_PLAYLIST_INDEX;
    private final List<MediaItem> playlist;

    @Inject
    public PlaylistManager(@NonNull List<MediaItem> initialPlayList, @NonNull int startIndex) {
        this.playlist = initialPlayList;
        this.queueIndex = startIndex;
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
