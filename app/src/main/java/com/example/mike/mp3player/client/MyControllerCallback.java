package com.example.mike.mp3player.client;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyControllerCallback extends MediaControllerCompat.Callback {
    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {}

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {}
}
