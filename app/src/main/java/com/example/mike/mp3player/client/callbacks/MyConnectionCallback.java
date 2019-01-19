package com.example.mike.mp3player.client.callbacks;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserConnector;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

    private MediaBrowserConnector mediaBrowserConnector;

    public MyConnectionCallback(MediaBrowserConnector mediaBrowserConnector) {
        super();
        this.mediaBrowserConnector = mediaBrowserConnector;
    }

    @Override
    public void onConnected() {
        mediaBrowserConnector.onConnected(null);
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
