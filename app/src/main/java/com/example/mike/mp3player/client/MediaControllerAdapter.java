package com.example.mike.mp3player.client;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

public class MediaControllerAdapter {

    private static final String LOG_TAG = "MDIA_CNTRLLR_ADPTR";
    private MediaControllerCompat mediaControllerCompat;
    private MyMediaControllerCallback myMediaControllerCallback;
    private MediaActivityCompat activity;
    private MediaSessionCompat.Token token;
    private boolean isInitialized = false;
    private Context context;
    private Looper looper;

    public MediaControllerAdapter(MediaActivityCompat activity, MediaSessionCompat.Token token, Looper looper) {
        this.activity = activity;
        this.token = token;
        this.context = activity.getApplicationContext();
        this.looper = looper;
    }

    public boolean init() {
        try {
            this.mediaControllerCompat = new MediaControllerCompat(activity.getApplicationContext(), token);
            this.myMediaControllerCallback = new MyMediaControllerCallback(looper);
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

    public void setRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
        getMediaControllerCompat().getTransportControls().setRepeatMode(repeatMode);
    }

    public Context getContext() {
        return context;
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
        myMediaControllerCallback.getMyMetaDataCallback().registerMetaDataListener(metaDataListener);
    }

    public void unregisterMetaDataListener(MetaDataListener metaDataListener) {
        myMediaControllerCallback.getMyMetaDataCallback().removeMetaDataListener(metaDataListener);
    }

    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener, ListenerType listenerType) {
        myMediaControllerCallback.getMyPlaybackStateCallback().registerPlaybackStateListener(playbackStateListener, listenerType);
    }

    public void unregisterPlaybackStateListener(PlaybackStateListener playbackStateListener, ListenerType listenerType) {
        myMediaControllerCallback.getMyPlaybackStateCallback().removePlaybackStateListener(playbackStateListener, listenerType);
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

    public MediaSessionCompat.Token getToken() {
        return token;
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
