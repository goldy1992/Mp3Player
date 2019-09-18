package com.example.mike.mp3player.client.callbacks.playback;

import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.client.callbacks.AsyncCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class MyPlaybackStateCallback extends AsyncCallback<PlaybackStateCompat> {
    private static final String LOG_TAG = "MY_PLYBK_ST_CLLBK";
    private final Map<PlaybackStateListener, Long> listenerToActionMap;
    private final Set<PlaybackStateListener> listeners;

    @Inject
    public MyPlaybackStateCallback(Handler handler) {
        super(handler);
        this.listenerToActionMap = new HashMap<>();
        this.listeners = new HashSet<>();
    }

    public void updateAll(PlaybackStateCompat state) {
        for (PlaybackStateListener listener : listenerToActionMap.keySet()) {
            listener.onPlaybackStateChanged(state);
        }
    }

    @Override
    public void processCallback(PlaybackStateCompat data) {
      //  long actions = data.getActions();
        for (PlaybackStateListener listener : listeners) {
            listener.onPlaybackStateChanged(data);
        }


//        for (PlaybackStateListener listener : listenerToActionMap.keySet()) {
//            Long listenerActions = listenerToActionMap.get(listener);
//            boolean notifyListener = listenerActions != null && (actions & listenerActions) != 0;
//            if (notifyListener) {
//                listener.onPlaybackStateChanged(data);
//            }
//        }
    }

    public synchronized void registerPlaybackStateListener(PlaybackStateListener listener, Set<ListenerType> listenerTypes) {
        listeners.add(listener);
        long actions = 0;
        for (ListenerType listenerType : listenerTypes) {
            actions |= listenerType.getActions();
        }
        listenerToActionMap.put(listener, actions);
        //Log.i(LOG_TAG, "registerPlaybackListener" + listener.getClass());
    }
    /**
     * @param listener the PlaybackStateListener to be removed
     * @return true if the listener was removed successfully
     */
    public synchronized boolean removePlaybackStateListener(PlaybackStateListener listener) {
        boolean result = false;
        if (listenerToActionMap.containsKey(listener)) {
                listenerToActionMap.remove(listener);
                result = true;
            }
        return result;
    }
    /** @return the listenerToActionMap */
    @VisibleForTesting
    public Map<PlaybackStateListener, Long> getListenerToActionMap() {
        return listenerToActionMap;
    }
}
