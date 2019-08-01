package com.example.mike.mp3player.client.callbacks.connection;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;

import javax.inject.Inject;

/**
 * Created by Mike on 04/10/2017.
 */
public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

    private MediaBrowserConnectorCallback mediaBrowserConnectorCallback;

    @Inject
    public MyConnectionCallback(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        super();
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }

    @Override
    public void onConnected() {
     if (null != mediaBrowserConnectorCallback) {
         mediaBrowserConnectorCallback.onConnected();
     }
   }

    @Override
    public void onConnectionSuspended() {
        // The Service has crashed. Disable transport controls until it automatically reconnects
    }

    @Override
    public void onConnectionFailed() {
        // The Service has refused our connection
    }

    public void setMediaBrowserConnectorCallback(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }
}
