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

/**
 * Created by Mike on 04/10/2017.
 */

public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
    private Context context;
    private MediaPlayerActivity mediaPlayerActivity;
    private MyMediaControllerCallback controllerCallback;

    public MyConnectionCallback(
                                MediaPlayerActivity mediaPlayerActivity,
                                MyMediaControllerCallback controllerCallback)
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
        MediaControllerCompat mediaController =  null;
        try {
            mediaController = new MediaControllerCompat(context, token);
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
    {
        // Grab the view for the play/pause button
        View mPlayPause = (ImageView) mediaPlayerActivity.findViewById(R.id.playPauseButton);

        // Attach a listener to the button
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Since this is a play/pause button, you'll need to test the current state
                // and choose the action accordingly

                int pbState = MediaControllerCompat.getMediaController(mediaPlayerActivity).getPlaybackState().getState();
                if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                    MediaControllerCompat.getMediaController(mediaPlayerActivity).getTransportControls().pause();
                } else {
                    MediaControllerCompat.getMediaController(mediaPlayerActivity).getTransportControls().play();
                }
            }
        });

            MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(mediaPlayerActivity);

            // Display the initial state
            MediaMetadataCompat metadata = mediaController.getMetadata();
            PlaybackStateCompat pbState = mediaController.getPlaybackState();

            // Register a Callback to stay in sync
        mediaController.registerCallback(controllerCallback);
    }

}
