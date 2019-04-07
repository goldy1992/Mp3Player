package com.example.mike.mp3player.client.callbacks;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;

/**
 * Created by Mike on 04/10/2017.
 */
public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

    private MediaBrowserConnectorCallback mediaBrowserConnectorCallback;

    public MyConnectionCallback(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        super();
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }

    @Override
    public void onConnected() {
        mediaBrowserConnectorCallback.onConnected();
   }

    @Override
    public void onConnectionSuspended() {
        // The Service has crashed. Disable transport controls until it automatically reconnects
    }

    @Override
    public void onConnectionFailed() {
        // The Service has refused our connection
    }
}
