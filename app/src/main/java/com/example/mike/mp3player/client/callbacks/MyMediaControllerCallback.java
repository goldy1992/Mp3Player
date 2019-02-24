package com.example.mike.mp3player.client.callbacks;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MetaDataListener;
import com.example.mike.mp3player.client.PlaybackStateListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.commons.Constants;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mike on 04/10/2017.
 */
// TODO: ORGANIZE LISTENERS INTO CATEGORIES DEFINED BY THE ACTION THAT SHOULD BE SET IN THE ACTIONS LIST
public class MyMediaControllerCallback extends MediaControllerCompat.Callback {

    private static final String LOG_TAG = "MY_MDIA_CNTLR_CLLBCK";
    private final MediaActivityCompat activity;
    private final MediaControllerAdapter mediaControllerAdapter;
    private Set<MetaDataListener> metaDataListeners;
    private Set<PlaybackStateListener> playbackStateListeners;

    public MyMediaControllerCallback(MediaActivityCompat activity, MediaControllerAdapter mediaControllerAdapter) {
        this.activity = activity;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.metaDataListeners = new HashSet<>();
        this.playbackStateListeners = new HashSet<>();
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        StringBuilder sb = new StringBuilder();
        for (MetaDataListener listener : metaDataListeners) {
            if (null != listener) {
                listener.onMetadataChanged(metadata);
                sb.append(listener.getClass());
            }
        }
        Log.i(LOG_TAG, "hit meta data changed " + ", listeners " + metaDataListeners.size() + ", " + sb.toString());
    }

    public synchronized void registerMetaDataListener(MetaDataListener listener) {
        Log.i(LOG_TAG, "registerMetaDataListener" + listener.getClass());
        metaDataListeners.add(listener);
    }

    public synchronized void registerMetaDataListeners(Collection<MetaDataListener> listeners) {
        metaDataListeners.addAll(listeners);
    }

    public synchronized boolean removeMetaDataListener(MetaDataListener listener) {
        return metaDataListeners.remove(listener);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        // TODO: make use of the actions in state to direct updates to the relevant listeners only

        StringBuilder sb = new StringBuilder();
        for (PlaybackStateListener listener : playbackStateListeners) {
            if (listener != null) {
                listener.onPlaybackStateChanged(state);
                sb.append(listener.getClass());
            } else {

            }
        }
        Log.i(LOG_TAG, "hit playback state changed with status " + Constants.playbackStateDebugMap.get(state.getState()) + ", listeners " + playbackStateListeners.size() + ", " + sb.toString());
    }

    public synchronized void registerPlaybackStateListener(PlaybackStateListener listener) {
        Log.i(LOG_TAG, "registerPlaybackListener" + listener.getClass());
        playbackStateListeners.add(listener);
    }

    public synchronized void registerPlaybackStateListeners(Collection<PlaybackStateListener> listeners) {
        playbackStateListeners.addAll(listeners);
    }

    public synchronized boolean removePlaybackStateListener(PlaybackStateListener listener) {
        return playbackStateListeners.remove(listener);
    }
}
