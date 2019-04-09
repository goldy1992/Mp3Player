package com.example.mike.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class PlaybackManager {

    private static final int START_OF_PLAYLIST = 0;
    private int queueIndex = -1;
    private boolean isRepeating = true;
    private final List<QueueItem> playlist = new ArrayList<>();

    private boolean shuffleOn = false;

    public PlaybackManager(@NonNull List<QueueItem> queueItems) {
        playlist.addAll(queueItems);
        queueIndex = START_OF_PLAYLIST;

    }

    public List<QueueItem> onAddQueueItem(QueueItem item) {
        playlist.add(item);
        queueIndex = (queueIndex == -1) ? 0 : queueIndex;
        return playlist;
    }

    public List<QueueItem> onRemoveQueueItem(QueueItem item) {
        boolean removed = playlist.remove(item);
        if (removed) {
            queueIndex = (playlist.isEmpty()) ? -1 : queueIndex;
        }
        return playlist;
    }

    public void notifyPlaybackComplete() {
        queueIndex++;
        if (isRepeating && (queueIndex >= playlist.size())) {
            queueIndex = START_OF_PLAYLIST;
        }
    }

    public String getNext() {
        int newIndex = getNextQueueItemIndex();
        return getPlaylistMediaId(newIndex);
    }

    public String skipToNext() {
        return incrementQueue() ? getCurrentMediaId() : null;
    }

    public String skipToPrevious() {
        return decrementQueue() ? getCurrentMediaId() : null;
    }

    private boolean incrementQueue() {
        int newIndex = getNextQueueItemIndex();
        if (newIndex >= 0) {
            this.queueIndex = newIndex;
            return true;
        }
        return false;
    }

    private boolean decrementQueue() {
        int newIndex = getPreviousQueueItemIndex();
        if (newIndex >= 0) {
            this.queueIndex = newIndex;
            return true;
        }
        return false;
    }

    private int getNextQueueItemIndex() {
        int newIndex = queueIndex + 1;
        boolean passedEndOfQueue = newIndex >= playlist.size();
        if (passedEndOfQueue) {
            return isRepeating ? START_OF_PLAYLIST : -1;
        }
        return newIndex;
    }

    private int getPreviousQueueItemIndex() {
        int newIndex = queueIndex - 1;
        boolean beforeStartOfQueue = newIndex < START_OF_PLAYLIST;
        if (beforeStartOfQueue) {
            return isRepeating ? getLastIndex() : -1;
        }
        return newIndex;
    }

    public boolean validQueueIndex(int newQueueIndex) {
        return newQueueIndex < playlist.size() && newQueueIndex >= 0;
    }

    public String getPlaylistMediaId(int index) {
        if (validQueueIndex(index)) {
            QueueItem queueItem = playlist.get(index);
            if (queueItem != null) {
                return playlist.get(index).getDescription().getMediaId();
            }
        }
        return null;
    }

    public String getCurrentMediaId() {
        if (playlist != null && !playlist.isEmpty() && playlist.get(queueIndex) != null) {
            QueueItem currentItem = playlist.get(queueIndex);
            if (currentItem.getDescription() != null) {
                return  currentItem.getDescription().getMediaId();
            }
        }
        return null;
    }

    public boolean createNewPlaylist(List<QueueItem> newList) {
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

    public QueueItem getCurrentItem() {
        if (validQueueIndex(queueIndex)) {
            return playlist.get(queueIndex);
        }
        return  null;
    }

    public int getLastIndex() {
        return playlist.size() - 1;
    }

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public int getQueueSize() {
        return playlist.size();
    }
}
