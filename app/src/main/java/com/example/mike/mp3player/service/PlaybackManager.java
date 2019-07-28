package com.example.mike.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.inject.Inject;

public class PlaybackManager {

    private static final int START_OF_PLAYLIST = 0;
    private static final int EMPTY_PLAYLIST_INDEX = -1;
    private Stack<Integer> shufflePreviousStack = new Stack<>();
    private Stack<Integer> shuffleNextStack = new Stack<>();
    private int nextShuffledIndex = 0;
    private int queueIndex = EMPTY_PLAYLIST_INDEX;
    private Random random = new Random();
    private boolean isRepeating = true;
    private final List<QueueItem> playlist = new ArrayList<>();
    private boolean shuffleOn = false;

    @Inject
    public PlaybackManager() { }

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
        if (isShuffleOn()) {
            shufflePreviousStack.push(new Integer(queueIndex));
            if (shuffleNextStack.empty()) {
                queueIndex = nextShuffledIndex;
                nextShuffledIndex = getNextShuffledIndex();
            } else {
                queueIndex = shuffleNextStack.pop();
            }
        } else {
            queueIndex++;
            if (isRepeating && (queueIndex >= playlist.size())) {
                queueIndex = START_OF_PLAYLIST;
            }
        }
    }

    public String getNext() {
        int newIndex;
        if (isShuffleOn()) {
            if (shuffleNextStack.empty()) {
                newIndex =  nextShuffledIndex;
            } else {
                newIndex = shuffleNextStack.peek();
            }
        } else {
            newIndex = getNextQueueItemIndex();
        }
        return getPlaylistMediaId(newIndex);
    }

    public String skipToNext() {
        if (isShuffleOn()) {
            shufflePreviousStack.push(new Integer(queueIndex));
            if (shuffleNextStack.empty()) {
                String toReturn = getPlaylistMediaId(this.nextShuffledIndex);
                this.queueIndex = this.nextShuffledIndex;
                this.nextShuffledIndex = getNextShuffledIndex();
                return toReturn;
            } else {
                this.queueIndex = shuffleNextStack.pop();
                return  getPlaylistMediaId(this.queueIndex);
            }
        } else {
            return incrementQueue() ? getCurrentMediaId() : null;
        }
    }

    public String skipToPrevious() {
        if (isShuffleOn()) {
            if (shufflePreviousStack.empty()) { // there's no previous available so keep same track
                return getPlaylistMediaId(queueIndex);
            } else { // get prevo=ious and add to the next stack;
                shuffleNextStack.push(new Integer(queueIndex));
                queueIndex = shufflePreviousStack.pop();
                return getPlaylistMediaId(queueIndex);
            }
        } else {
            return decrementQueue() ? getCurrentMediaId() : null;
        }
    }

    private boolean incrementQueue() {
        int newIndex = getNextQueueItemIndex();
        if (validQueueIndex(newIndex)) {
            this.queueIndex = newIndex;
            return true;
        }
        return false;
    }

    private boolean decrementQueue() {
        int newIndex = getPreviousQueueItemIndex();
        if (validQueueIndex(newIndex)) {
            this.queueIndex = newIndex;
            return true;
        }
        return false;
    }
    /**
     * ASSUMES shuffleON IS FALSE
     * @return
     */
    private int getNextQueueItemIndex() {
        int newIndex = queueIndex + 1;
        boolean passedEndOfQueue = newIndex >= playlist.size();
        if (passedEndOfQueue) {
            return isRepeating ? START_OF_PLAYLIST : -1;
        }
        return newIndex;
    }
    /**
     * ASSUMES shuffleON IS FALSE
     * @return
     */
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
        playlist.clear();
        boolean result = playlist.addAll(newList);
        queueIndex = playlist.isEmpty() ?  EMPTY_PLAYLIST_INDEX : START_OF_PLAYLIST;
        return result;
    }

    public void setCurrentItem(String mediaId) {
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

    private int getNextShuffledIndex() {
        this.nextShuffledIndex = shuffleNextStack.empty() ? shuffleNewIndex() : shuffleNextStack.pop();
        return this.nextShuffledIndex;
    }

    @VisibleForTesting()
    public int shuffleNewIndex() {
        return random.nextInt(getQueueSize());
    }

    public void setShuffle(boolean shuffleOn) {
        if (shuffleOn == this.isShuffleOn()) {
            return;
        }
        this.shuffleOn = shuffleOn;
        if (shuffleOn) {
            this.shuffleNextStack.clear();
            this.shufflePreviousStack.clear();
            this.nextShuffledIndex = shuffleNewIndex();
        }
    }

    @PlaybackStateCompat.ShuffleMode
    public int getShuffleMode() {
        if (shuffleOn)
        {
            return PlaybackStateCompat.SHUFFLE_MODE_ALL;
        }
        return PlaybackStateCompat.SHUFFLE_MODE_NONE;
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

    public boolean isShuffleOn() { return shuffleOn; }

    public boolean isInitialised() {
        return !playlist.isEmpty();
    }
}
