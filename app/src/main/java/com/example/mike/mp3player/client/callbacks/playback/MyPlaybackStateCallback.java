package com.example.mike.mp3player.client.callbacks.playback;

import android.os.Looper;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.callbacks.AsyncCallback;
import com.example.mike.mp3player.commons.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.mike.mp3player.commons.Constants.NO_ACTION;

public class MyPlaybackStateCallback extends AsyncCallback<PlaybackStateCompat> {
    private static final String LOG_TAG = "MY_PLYBK_ST_CLLBK";
    private Map<ListenerType, Set<PlaybackStateListener>> listeners;

    public MyPlaybackStateCallback(Looper looper) {
        super(looper);
        this.listeners = new HashMap<>();
    }

    @Override
    public void processCallback(PlaybackStateCompat data) {
        // TODO: make use of the actions in state to direct updates to the relevant listeners only

        long actions = data.getActions();
        if (containsAction(actions, PlaybackStateCompat.ACTION_PLAY) || containsAction(actions, PlaybackStateCompat.ACTION_PAUSE) || containsAction(actions, PlaybackStateCompat.ACTION_SEEK_TO)) {
            notifyListenersOfType(ListenerType.PLAYBACK, data);
        }

        if (containsAction(actions, PlaybackStateCompat.ACTION_SEEK_TO)) {
            notifyListenersOfType(ListenerType.PLAYBACK, data);
        }

        if (containsAction(actions, NO_ACTION)) {
            notifyListenersOfType(ListenerType.MISC, data);
        }
    }

    public synchronized void registerPlaybackStateListener(PlaybackStateListener listener, ListenerType listenerType) {
        if (!listeners.containsKey(listenerType)) {
            listeners.put(listenerType, new HashSet<>());
        }
        listeners.get(listenerType).add(listener);
        Log.i(LOG_TAG, "registerPlaybackListener" + listener.getClass());
    }


    public synchronized boolean removePlaybackStateListener(PlaybackStateListener listener, ListenerType listenerType) {
        boolean result = false;
        if (listeners.containsKey(listenerType)) {
            Set<PlaybackStateListener> listenersValue = listeners.get(listenerType);
            result = listenersValue.remove(listener);
            if (listenersValue.isEmpty()) {
                listeners.remove(listenerType);
            }
        }
        return result;
    }

    private boolean containsAction(long actions, long action) {
        return (actions & action) != 0;
    }

    private void notifyListenersOfType(ListenerType listenerType, PlaybackStateCompat state) {
        Set<PlaybackStateListener> listenersSet = listeners.get(listenerType);
        StringBuilder sb = new StringBuilder();
        if (listenersSet != null) {
            for (PlaybackStateListener listener : listenersSet) {
                listener.onPlaybackStateChanged(state);
                sb.append(listener.getClass());
            }
        }
        Log.i(LOG_TAG, "hit playback state changed with status " + Constants.playbackStateDebugMap.get(state.getState()) + ", listeners " + listenersSet.size() + ", " + sb.toString());
    }
}
