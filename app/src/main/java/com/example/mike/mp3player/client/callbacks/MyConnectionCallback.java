package com.example.mike.mp3player.client.callbacks;

import android.content.Context;
import android.media.browse.MediaBrowser;
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
import com.example.mike.mp3player.client.MediaPlayerActivity;
import com.example.mike.mp3player.client.view.SeekerBar;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

    private MediaPlayerActivity mediaPlayerActivity;

    MyConnectionCallback(MediaPlayerActivity mediaPlayerActivity) {
        super();
        this.mediaPlayerActivity = mediaPlayerActivity;
    }

    @Override
    public void onConnected() {
            mediaPlayerActivity.onConnected();
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
