package com.example.mike.mp3player.client.callbacks.playback;

import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.callbacks.AsyncCallback;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class MyPlaybackStateCallback extends AsyncCallback<PlaybackStateCompat> {
    private static final String LOG_TAG = "MY_PLYBK_ST_CLLBK";
    private final Set<PlaybackStateListener> listeners;

    @Inject
    public MyPlaybackStateCallback(Handler handler) {
        super(handler);
        this.listeners = new HashSet<>();
    }

    @Override
    public void processCallback(PlaybackStateCompat data) {
        for (PlaybackStateListener listener : listeners) {
            listener.onPlaybackStateChanged(data);
        }
    }

    public synchronized void registerPlaybackStateListener(PlaybackStateListener listener) {
        listeners.add(listener);
    }
    /**
     * @param listener the PlaybackStateListener to be removed
     * @return true if the listener was removed successfully
     */
    public synchronized boolean removePlaybackStateListener(PlaybackStateListener listener) {
        return listeners.remove(listener);
    }

    public Set<PlaybackStateListener> getListeners() {
        return listeners;
    }
}
