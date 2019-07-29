package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Collections;

import javax.inject.Inject;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;

public class PlayPauseButton extends LinearLayoutWithImageView implements PlaybackStateListener {

    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";
    @PlaybackStateCompat.State
    private int state = PlaybackStateCompat.STATE_NONE;
    private MediaControllerAdapter mediaControllerAdapter;

    public PlayPauseButton(Context context) { this(context, null); }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Inject
    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr,
                           @NonNull MediaControllerAdapter mediaControllerAdapter) {
        super(context, attrs, defStyleAttr, 0, 2);
        this.state = mediaControllerAdapter.getPlaybackState();
        @DrawableRes int initialImage = state == STATE_PLAYING ? R.drawable.ic_baseline_pause_24px : R.drawable.ic_baseline_play_arrow_24px;
        init(initialImage);
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mediaControllerAdapter.registerPlaybackStateListener(this, Collections.singleton(ListenerType.PLAYBACK));

        getView().setOnClickListener(this::playPause);
    }

    @VisibleForTesting()
    public void playPause(View view) {
        if (getState() == PlaybackStateCompat.STATE_PLAYING) {
            Log.d(LOG_TAG, "calling pause");
            mediaControllerAdapter.pause();
        } else {
            Log.d(LOG_TAG, "calling play");
            mediaControllerAdapter.play();
        }
    }
    private void updateState(int newState) {
        if (newState != getState()) {
            switch (newState) {
                case STATE_PLAYING:
                    mainUpdater.post(this::setPauseIcon);
                    this.state = STATE_PLAYING;
                    break;
                case STATE_PAUSED:
                    mainUpdater.post(this::setPlayIcon);
                    this.state = STATE_PAUSED;
                    break;
                default:
                    Log.e(LOG_TAG, "invalid state hit for PlayPauseButton state");
            } // switch
        }
    }

    private synchronized void setPlayIcon() {
        setViewImage(R.drawable.ic_baseline_play_arrow_24px);
    }
    private synchronized void setPauseIcon() {
        setViewImage(R.drawable.ic_baseline_pause_24px);
    }

    @PlaybackStateCompat.State
    public int getState() {
        return state;
    }

    @VisibleForTesting
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
        updateState(state.getState());
    }
}
