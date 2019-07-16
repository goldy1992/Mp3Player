package com.example.mike.mp3player.client;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Set;

import javax.inject.Inject;

public class MediaControllerAdapter {

    private static final String LOG_TAG = "MDIA_CNTRLLR_ADPTR";
    private MediaControllerCompat mediaController;
    private MyMediaControllerCallback myMediaControllerCallback;
    private MediaSessionCompat.Token token = null;
    private final Context context;

    @Inject
    public MediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        this.context = context;
        this.myMediaControllerCallback = myMediaControllerCallback;
    }

    public void setMediaToken(@NonNull MediaSessionCompat.Token token) {
        if (!isInitialized()) {
            init(token);
        } else {
            Log.e(LOG_TAG, "MediaControllerAdapter already initialised");
        }
    }

    @VisibleForTesting
    public void init(@NonNull MediaSessionCompat.Token token) {
        try {
            this.mediaController = new MediaControllerCompat(context, token);
            this.mediaController.registerCallback(myMediaControllerCallback);
        } catch (RemoteException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
        }
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

    public void unregisterPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        myMediaControllerCallback.getMyPlaybackStateCallback().removePlaybackStateListener(playbackStateListener);
    }

    public int getPlaybackState() {
        if (mediaController != null && mediaController.getPlaybackState() != null) {
            return mediaController.getPlaybackState().getState();
        }
        return 0;
    }

    public PlaybackStateCompat getPlaybackStateObject() {
        if (mediaController != null && mediaController.getPlaybackState() != null) {
            return mediaController.getPlaybackState();
        }
        return null;
    }

    public MediaMetadataCompat getMetadata() {
        if (mediaController != null && mediaController.getPlaybackState() != null) {
            return mediaController.getMetadata();
        }
        return null;
    }

    public MediaSessionCompat.Token getToken() {
        return token;
    }

    public void disconnect() {
        if (mediaController != null && myMediaControllerCallback != null) {
            mediaController.unregisterCallback(myMediaControllerCallback);
        }
    }

    public boolean isInitialized() {
        return mediaController != null && mediaController.isSessionReady();
    }

    public void updateUiState() {
        if (isInitialized()) {
            myMediaControllerCallback.onMetadataChanged(mediaController.getMetadata());
            myMediaControllerCallback.getMyPlaybackStateCallback().updateAll(mediaController.getPlaybackState());
        }
    }

    public void sendCustomAction(String customAction, Bundle args) {
        getController().sendCustomAction(customAction, args);
    }

    @VisibleForTesting
    public MediaControllerCompat.TransportControls getController() {
        return mediaController.getTransportControls();
    }

    @VisibleForTesting
    public MediaControllerCompat getMediaController() {
        return mediaController;
    }

    @VisibleForTesting
    public void setMediaController(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }
}
