package com.example.mike.mp3player.client.callbacks;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MetaDataListener;
import com.example.mike.mp3player.client.PlaybackStateListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyMediaControllerCallback extends MediaControllerCompat.Callback {

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
        for (MetaDataListener listener : metaDataListeners) {
            listener.onMetadataChanged(metadata);
        }
    }

    public synchronized void registerMetaDataListener(MetaDataListener listener) {
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
        for (PlaybackStateListener listener : playbackStateListeners) {
            listener.onPlaybackStateChanged(state);
        }
    }

    public synchronized void registerPlaybackStateListener(PlaybackStateListener listener) {
        playbackStateListeners.add(listener);
    }

    public synchronized void registerPlaybackStateListeners(Collection<PlaybackStateListener> listeners) {
        playbackStateListeners.addAll(listeners);
    }

    public synchronized boolean removePlaybackStateListener(PlaybackStateListener listener) {
        return playbackStateListeners.remove(listener);
    }
}
