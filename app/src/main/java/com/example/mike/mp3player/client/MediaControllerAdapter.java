package com.example.mike.mp3player.client;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;

public class MediaControllerAdapter {

    private static final String LOG_TAG = "MDIA_CNTRLLR_ADPTR";
    private MediaControllerCompat mediaControllerCompat;
    private MyMediaControllerCallback myMediaControllerCallback;
    private MediaActivityCompat activity;
    private MediaSessionCompat.Token token;
    private boolean isInitialized = false;

    public MediaControllerAdapter(MediaActivityCompat activity, MediaSessionCompat.Token token) {
        this.activity = activity;
        this.token = token;
    }

    public boolean init() {
        try {
            this.mediaControllerCompat = new MediaControllerCompat(activity.getApplicationContext(), token);
            this.myMediaControllerCallback = new MyMediaControllerCallback(activity, this);
            this.mediaControllerCompat.registerCallback(myMediaControllerCallback);
        } catch (RemoteException ex) {
            this.isInitialized = false;
            return false;
        }

        this.isInitialized = true;
        return true;
    }

    public void prepareFromMediaId(String mediaId, Bundle extras) {
        getMediaControllerCompat().getTransportControls().prepareFromMediaId(mediaId, extras);
    }

    public void play() {

        Log.i(LOG_TAG, "play hit");
        getMediaControllerCompat().getTransportControls().play();
    }

    public void pause()
    {
        Log.i(LOG_TAG, "pause hit");
        getMediaControllerCompat().getTransportControls().pause();
    }

    public void seekTo(long position) {
        getMediaControllerCompat().getTransportControls().seekTo(position);
    }

    public void stop() {
        getMediaControllerCompat().getTransportControls().stop();
    }

    public void skipToNext() {
        getMediaControllerCompat().getTransportControls().skipToNext();
    }

    public void skipToPrevious() {
        getMediaControllerCompat().getTransportControls().skipToPrevious();
    }

    public void registerMetaDataListener(MetaDataListener metaDataListener) {
        myMediaControllerCallback.registerMetaDataListener(metaDataListener);
    }

    public void unregisterMetaDataListener(MetaDataListener metaDataListener) {
        myMediaControllerCallback.removeMetaDataListener(metaDataListener);
    }

    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        myMediaControllerCallback.registerPlaybackStateListener(playbackStateListener);
    }

    public void unregisterPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        myMediaControllerCallback.removePlaybackStateListener(playbackStateListener);
    }

    public int getPlaybackState() {
        if (getMediaControllerCompat() != null && getMediaControllerCompat().getPlaybackState() != null) {
            return getMediaControllerCompat().getPlaybackState().getState();
        }
        return 0;
    }

    public PlaybackStateCompat getPlaybackStateAsCompat() {
        if (getMediaControllerCompat() != null ) {
            return getMediaControllerCompat().getPlaybackState();
        }
        return null;
    }

    public MediaControllerCompat getMediaControllerCompat() {
        return mediaControllerCompat;
    }

    public MediaMetadataCompat getMetaData() {
        return mediaControllerCompat.getMetadata();
    }

    public void disconnect() {
        if (getMediaControllerCompat() != null && myMediaControllerCallback != null) {
//            if (!myMediaControllerCallback.getChildCallbacks().isEmpty()) {
//                // find a way to disconnect all callbacks
//                for (MediaControllerCompat.Callback callback : myMediaControllerCallback.getChildCallbacks()) {
//                    getMediaControllerCompat().unregisterCallback(callback);
//                }
//            }
            getMediaControllerCompat().unregisterCallback(myMediaControllerCallback);
        }
    }

    public void updateUiState() {
        myMediaControllerCallback.onMetadataChanged(mediaControllerCompat.getMetadata());
        myMediaControllerCallback.onPlaybackStateChanged(mediaControllerCompat.getPlaybackState());
    }

    public void sendCustomAction(String customAction, Bundle args) {
        getMediaControllerCompat().getTransportControls().sendCustomAction(customAction, args);
    }

    public PlaybackStateCompat getCurrentPlaybackState() {
        return mediaControllerCompat.getPlaybackState();
    }
}
