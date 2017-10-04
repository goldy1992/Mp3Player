package com.example.mike.mp3player;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.MediaPlayerActivity;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyConnectionCallback extends MediaBrowser.ConnectionCallback {
    private MediaBrowserCompat mMediaBrowser;
    private Context context;
    private MediaPlayerActivity mediaPlayerActivity;
    @Override
    public void onConnected() {

        // Get the token for the MediaSession
        MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();

        // Create a MediaControllerCompat
        MediaControllerCompat mediaController =
                null;
        try {
            mediaController = new MediaControllerCompat(context, // Context
                    token);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Save the controller
        MediaControllerCompat.setMediaController(mediaPlayerActivity, mediaController);

        // Finish building the UI
//        buildTransportControls();
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
