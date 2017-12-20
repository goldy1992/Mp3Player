package com.example.mike.mp3player.client;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.ConnectionCallback;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaPlayerActivity;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
    private Context context;
    private MediaPlayerActivity mediaPlayerActivity;
    private MyControllerCallback controllerCallback;

    public MyConnectionCallback(
                                MediaPlayerActivity mediaPlayerActivity,
                                MyControllerCallback controllerCallback)
    {
        super();
        this.mediaPlayerActivity = mediaPlayerActivity;
        this.context = mediaPlayerActivity.getApplicationContext();
        this.controllerCallback = controllerCallback;
    }

    @Override
    public void onConnected() {

        // Get the token for the MediaSession
        MediaSessionCompat.Token token = mediaPlayerActivity.getmMediaBrowser().getSessionToken();

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
        buildTransportControls();
    }

    @Override
    public void onConnectionSuspended() {
        // The Service has crashed. Disable transport controls until it automatically reconnects
    }

    @Override
    public void onConnectionFailed() {
        // The Service has refused our connection
    }

    void buildTransportControls()
    {}

}
