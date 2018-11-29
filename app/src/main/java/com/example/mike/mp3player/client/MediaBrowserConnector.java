package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.MySubscriptionCallback;
import com.example.mike.mp3player.service.MediaPlaybackService;

public class MediaBrowserConnector {

    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MySubscriptionCallback mySubscriptionCallback;
    private MediaControllerCompat mediaControllerCompat;
    private final MainActivity activity;
    private MediaSessionCompat.Token mediaSessionToken;

    public MediaBrowserConnector(MainActivity activity) {
        this.activity = activity;
    }

    public void init() {
        mConnectionCallbacks = new MyConnectionCallback(this);
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(activity.getApplicationContext(),
                new ComponentName(activity, MediaPlaybackService.class),
                mConnectionCallbacks,
                null);

        this.mySubscriptionCallback = new MySubscriptionCallback(activity);
        getmMediaBrowser().connect();
    }

    public void onConnected() {
        try {
            MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();
            this.mediaSessionToken = token;
            mediaControllerCompat = new MediaControllerCompat(activity.getApplicationContext(), token);
            mMediaBrowser.subscribe(mMediaBrowser.getRoot(), mySubscriptionCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public MediaBrowserCompat getmMediaBrowser() {
        return mMediaBrowser;
    }

    public void disconnect() {
        getmMediaBrowser().disconnect();
        if (getMediaControllerCompat() != null) {
            // find a way to disconnect all callbacks
//            mediaControllerCompat.
//            getMediaControllerCompat().unregisterCallback(myMediaControllerCallback);
        }
        getmMediaBrowser().disconnect();
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

    public int getPlaybackState() {
        if (mediaControllerCompat != null && mediaControllerCompat.getPlaybackState() != null) {
            return mediaControllerCompat.getPlaybackState().getState();
        }
        return 0;
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaSessionToken;
    }

    public MediaControllerCompat getMediaControllerCompat() {
        return mediaControllerCompat;
    }
}
