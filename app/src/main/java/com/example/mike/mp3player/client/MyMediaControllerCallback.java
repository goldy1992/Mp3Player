package com.example.mike.mp3player.client;

import android.animation.ValueAnimator;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Button;

import com.example.mike.mp3player.client.view.PlayPauseButton;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyMediaControllerCallback extends MediaControllerCompat.Callback implements ValueAnimator.AnimatorUpdateListener{

    private final MediaPlayerActivity activity;

    MyMediaControllerCallback(MediaPlayerActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {}

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        PlayPauseButton button = activity.getPlayPauseButton();
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING: button.setTextPause(); break;
            case PlaybackStateCompat.STATE_PAUSED: button.setTextPlay(); break;
            case PlaybackStateCompat.STATE_STOPPED: button.setTextPlay(); break;
        } // switch
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

    }
}
