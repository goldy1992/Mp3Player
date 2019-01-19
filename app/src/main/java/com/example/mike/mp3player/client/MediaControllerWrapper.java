package com.example.mike.mp3player.client;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;

public class MediaControllerWrapper< A extends MediaActivityCompat>  {

    private MediaControllerCompat mediaControllerCompat;
    private MyMediaControllerCallback<A> myMediaControllerCallback;
    private A activity;
    private MediaSessionCompat.Token token;
    private boolean isInitialized = false;

    public MediaControllerWrapper(A activity, MediaSessionCompat.Token token) {
        this.activity = activity;
        this.token = token;
    }

    public boolean init() {
        try {
            this.mediaControllerCompat = new MediaControllerCompat(activity.getApplicationContext(), token);
            this.myMediaControllerCallback = new MyMediaControllerCallback<>(activity, this);
            this.mediaControllerCompat.registerCallback(myMediaControllerCallback);
        } catch (RemoteException ex) {
            this.isInitialized = false;
            return false;
        }

        this.isInitialized = true;
        return true;
    }

    public boolean registerCallback(MediaControllerCompat.Callback callback) {
        getMediaControllerCompat().registerCallback(callback);
        return true;
    }

    public void prepareFromMediaId(String mediaId, Bundle extras) {
        getMediaControllerCompat().getTransportControls().prepareFromMediaId(mediaId, extras);
    }

    public void play() {
        getMediaControllerCompat().getTransportControls().play();
    }

    public void pause() {
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
            if (!myMediaControllerCallback.getChildCallbacks().isEmpty()) {
                // find a way to disconnect all callbacks
                for (MediaControllerCompat.Callback callback : myMediaControllerCallback.getChildCallbacks()) {
                    getMediaControllerCompat().unregisterCallback(callback);
                }
            }
            getMediaControllerCompat().unregisterCallback(myMediaControllerCallback);
        }
    }

    public PlaybackStateCompat getCurrentPlaybackState() {
        return mediaControllerCompat.getPlaybackState();
    }
}
