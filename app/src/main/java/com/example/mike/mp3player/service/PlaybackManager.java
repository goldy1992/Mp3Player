package com.example.mike.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.ArrayList;
import java.util.List;

public class PlaybackManager {

    private int queueIndex = -1;
    private final List<MediaSessionCompat.QueueItem> playlist = new ArrayList<>();

    private boolean shuffleOn = false;
    private int repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL;

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
        return playlist.get(index).getDescription().getMediaId();
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
}
