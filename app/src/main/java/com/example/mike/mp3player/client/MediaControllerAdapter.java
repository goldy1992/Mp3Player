package com.example.mike.mp3player.client;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Set;

import javax.inject.Inject;

public class MediaControllerAdapter {

    private static final String LOG_TAG = "MDIA_CNTRLLR_ADPTR";
    private MediaControllerCompat mediaControllerCompat;
    private MyMediaControllerCallback myMediaControllerCallback;
    private MediaSessionCompat.Token token = null;
    private boolean isInitialized = false;
    private Context context;

    @Inject
    public MediaControllerAdapter() { }

    public MediaControllerAdapter(MediaSessionCompat.Token token) {
        init(token);
    }

    public void setMediaToken(MediaSessionCompat.Token token) {
        if (!isInitialized) {
            init(token);
        } else {
            Log.e(LOG_TAG, "MediaControllerAdapter already initialised");
        }
    }

    private void init(MediaSessionCompat.Token token) {
        boolean result = true;
        try {
            this.mediaControllerCompat = new MediaControllerCompat(context, token);
            this.mediaControllerCompat.registerCallback(myMediaControllerCallback);
        } catch (RemoteException ex) {
            result = false;
        }
        this.isInitialized = result;
        this.token = token;
        updateUiState();
    }

    public void prepareFromMediaId(String mediaId, Bundle extras) {
        getController().prepareFromMediaId(mediaId, extras);
    }

    public void play() {
        getController().play();
    }

    public void setRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
        getController().setRepeatMode(repeatMode);
    }

    public Context getContext() {
        return context;
    }

    public void pause() {
        //Log.i(LOG_TAG, "pause hit");
        getController().pause();
    }

    public void seekTo(long position) {
        getController().seekTo(position);
    }

    public void stop() {
        getController().stop();
    }

    public void skipToNext() {
        getController().skipToNext();
    }

    public void skipToPrevious() {
        getController().skipToPrevious();
    }

    public void setShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        getController().setShuffleMode(shuffleMode);
    }

    public void registerMetaDataListener(MetaDataListener metaDataListener) {
        myMediaControllerCallback.getMyMetaDataCallback().registerMetaDataListener(metaDataListener);
    }

    public void unregisterMetaDataListener(MetaDataListener metaDataListener) {
        myMediaControllerCallback.getMyMetaDataCallback().removeMetaDataListener(metaDataListener);
    }

    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener, Set<ListenerType> listenerTypes) {
        myMediaControllerCallback.getMyPlaybackStateCallback().registerPlaybackStateListener(playbackStateListener, listenerTypes);
    }

    public void unregisterPlaybackStateListener(PlaybackStateListener playbackStateListener, ListenerType listenerType) {
        myMediaControllerCallback.getMyPlaybackStateCallback().removePlaybackStateListener(playbackStateListener, listenerType);
    }

    public int getPlaybackState() {
        if (mediaControllerCompat != null && mediaControllerCompat.getPlaybackState() != null) {
            return mediaControllerCompat.getPlaybackState().getState();
        }
        return 0;
    }

    public PlaybackStateCompat getPlaybackStateAsCompat() {
        if (mediaControllerCompat != null ) {
            return mediaControllerCompat.getPlaybackState();
        }
        return null;
    }

    public MediaSessionCompat.Token getToken() {
        return token;
    }

    public void disconnect() {
        if (mediaControllerCompat != null && myMediaControllerCallback != null) {
            mediaControllerCompat.unregisterCallback(myMediaControllerCallback);
        }
    }

    public void updateUiState() {
        if (isInitialized) {
            myMediaControllerCallback.onMetadataChanged(mediaControllerCompat.getMetadata());
            myMediaControllerCallback.getMyPlaybackStateCallback().updateAll(mediaControllerCompat.getPlaybackState());
        }
    }

    public void sendCustomAction(String customAction, Bundle args) {
        getController().sendCustomAction(customAction, args);
    }
    
    private MediaControllerCompat.TransportControls getController() {
        return mediaControllerCompat.getTransportControls();
    }

    @Inject
    public void setMyMediaControllerCallback(MyMediaControllerCallback myMediaControllerCallback) {
        this.myMediaControllerCallback = myMediaControllerCallback;
    }

    @Inject
    public void setContext(Context context) {
        this.context = context;
    }
}
