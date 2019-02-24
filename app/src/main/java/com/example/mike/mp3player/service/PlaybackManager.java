package com.example.mike.mp3player.service;

import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PlaybackManager {

    private static final int START_OF_PLAYLIST = 0;
    private int queueIndex = -1;
    private final List<MediaSessionCompat.QueueItem> playlist = new ArrayList<>();

    private boolean shuffleOn = false;
    private int repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL;

    public void init(List<MediaSessionCompat.QueueItem> queueItems) {
        playlist.addAll(queueItems);
        queueIndex = START_OF_PLAYLIST;
    }

    public List<MediaSessionCompat.QueueItem> onAddQueueItem(MediaSessionCompat.QueueItem item) {
        playlist.add(item);
        queueIndex = (queueIndex == -1) ? 0 : queueIndex;
        return playlist;
    }

    public List<MediaSessionCompat.QueueItem> onRemoveQueueItem(MediaSessionCompat.QueueItem item) {
        playlist.remove(item);
        queueIndex = (playlist.isEmpty()) ? -1 : queueIndex;
        return playlist;
    }

    public void notifyPlaybackComplete() {
        queueIndex++;
    }

    public String playbackComplete() {
        queueIndex++;
        if (!playlist.isEmpty() && queueIndex < playlist.size()) {
            MediaSessionCompat.QueueItem nextItem = playlist.get(queueIndex);
            if (null != nextItem.getDescription()) {
                return nextItem.getDescription().getMediaId();
            }
        }
        return null;
    }

    public String getNext() {
        return getPlaylistMediaId(queueIndex + 1);
    }
    public String skipToNext() {
        return setNewPlaylistItem(queueIndex + 1) ? getPlaylistMediaId(queueIndex) : null;
    }


    public String skipToPrevious() {
        return setNewPlaylistItem(queueIndex - 1) ? getPlaylistMediaId(queueIndex) : null;
    }

    public boolean validQueueIndex(int newQueueIndex) {
        return newQueueIndex < playlist.size() && newQueueIndex >= 0;
    }

    public String getPlaylistMediaId(int index) {
        if (validQueueIndex(index)) {
            MediaSessionCompat.QueueItem queueItem = playlist.get(index);
            if (queueItem != null) {
                return playlist.get(index).getDescription().getMediaId();
            }
        }
        return null;
    }

    public String getCurrentMediaId() {
        return playlist.get(queueIndex).getDescription().getMediaId();
    }


    private boolean setNewPlaylistItem(int newIndex) {
        if (!validQueueIndex(newIndex)) {
            return false;
        }
            queueIndex = newIndex;
            return true;
    }
//            int currentState = myMediaPlayerAdapter.getCurrentState();
//            String newMediaId = playbackManager.getPlaylistMediaId(newIndex);
//            Uri newUri = mediaLibrary.getMediaUri(newMediaId);
//            myMediaPlayerAdapter.prepareFromUri(newUri);
//
//            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
//                myMediaPlayerAdapter.play();
//            }
//        }
//    }

    public boolean createNewPlaylist(List<MediaSessionCompat.QueueItem> newList) {
        playlist.clear();;
        return playlist.addAll(newList);
    }

    public void setQueueIndex(String mediaId) {
        Integer integerQueueIndex = MediaLibraryUtils.findIndexOfTrackInPlaylist(playlist, mediaId);
        if (integerQueueIndex == null) {
            queueIndex = -1;
        } else {
            queueIndex = integerQueueIndex;
        }
    }

    public MediaSessionCompat.QueueItem getCurrentItem() {
        return playlist.get(queueIndex);
    }

    public String getCurrentItemId() {
        return getCurrentItem().getDescription().getMediaId();
    }
}
