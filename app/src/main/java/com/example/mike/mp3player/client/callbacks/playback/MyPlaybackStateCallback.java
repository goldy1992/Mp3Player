package com.example.mike.mp3player.client.callbacks.playback;

import android.os.Looper;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.callbacks.AsyncCallback;
import com.example.mike.mp3player.commons.Constants;

import java.util.Collection;

public class MyPlaybackStateCallback extends AsyncCallback<PlaybackStateCompat> {
    private static final String LOG_TAG = "MY_PLYBK_ST_CLLBK";


    public MyPlaybackStateCallback(Looper looper) {
        super(looper);
    }

    @Override
    public void processCallback(PlaybackStateCompat data) {
        // TODO: make use of the actions in state to direct updates to the relevant listeners only

        StringBuilder sb = new StringBuilder();
        // TODO: Add logic to decide which listeners to update.
//        for (PlaybackStateListener listener : playbackStateListeners) {
//            if (listener != null) {
//                listener.onPlaybackStateChanged(state);
//                sb.append(listener.getClass());
//            } else {
//
//            }
//        }
       // Log.i(LOG_TAG, "hit playback state changed with status " + Constants.playbackStateDebugMap.get(data.getState()) + ", listeners " + playbackStateListeners.size() + ", " + sb.toString());
    }

    public synchronized void registerPlaybackStateListener(PlaybackStateListener listener, ListenerType listenerType) {
        Log.i(LOG_TAG, "registerPlaybackListener" + listener.getClass());

        switch (listenerType) {
            // TODO: add different lists for different listener types
        }
       // playbackStateListeners.add(listener);
    }


    public synchronized boolean removePlaybackStateListener(PlaybackStateListener listener, ListenerType listenerType) {
       // return playbackStateListeners.remove(listener);
        return true;
    }
}
