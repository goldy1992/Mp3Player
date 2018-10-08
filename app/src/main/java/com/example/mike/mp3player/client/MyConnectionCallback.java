package com.example.mike.mp3player.client;

import android.content.Context;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.SeekerBar;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

    private Context context;
    private MediaPlayerActivity mediaPlayerActivity;
    private SeekerBar seekerBar;

    private MyMediaControllerCallback controllerCallback;
    private MySeekerMediaControllerCallback seekerMediaControllerCallback;

    private MediaControllerCompat mediaControllerCompat;

    MyConnectionCallback(
            MediaPlayerActivity mediaPlayerActivity,
            SeekerBar seekerBar,
            MyMediaControllerCallback controllerCallback,
            MySeekerMediaControllerCallback seekerMediaControllerCallback)
    {
        super();
        this.mediaPlayerActivity = mediaPlayerActivity;
        this.seekerBar = seekerBar;

        this.context = mediaPlayerActivity.getApplicationContext();
        this.controllerCallback = controllerCallback;
        this.seekerMediaControllerCallback = seekerMediaControllerCallback;
    }

    @Override
    public void onConnected() {

        // Get the token for the MediaSession
        MediaSessionCompat.Token token = mediaPlayerActivity.getmMediaBrowser().getSessionToken();

        try {
            mediaControllerCompat = new MediaControllerCompat(context, token);
            mediaControllerCompat.registerCallback(controllerCallback);
            mediaControllerCompat.registerCallback(seekerMediaControllerCallback);

            mediaPlayerActivity.setMediaControllerCompat(mediaControllerCompat);
            seekerBar.setMediaController(mediaControllerCompat);
            // Display the initial state
            mediaControllerCompat.getMetadata();
            mediaControllerCompat.getPlaybackState();
            mediaControllerCompat.getTransportControls().prepareFromUri(mediaPlayerActivity.getSelectedUri(), null);
        }
        catch (RemoteException e) {
            e.printStackTrace();
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
}
