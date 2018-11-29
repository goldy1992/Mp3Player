package com.example.mike.mp3player.client.callbacks;

import android.media.MediaMetadata;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.MediaPlayerActivity;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyMediaControllerCallback extends MediaControllerCompat.Callback {

    private final MediaPlayerActivity activity;

    public MyMediaControllerCallback(MediaPlayerActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        activity.setMetaData(metadata);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        activity.setPlaybackState(state);
    }
}
