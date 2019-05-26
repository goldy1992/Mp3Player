package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;

public class PlayPauseButton extends LinearLayoutWithImageView implements PlaybackStateListener {

    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";
    @PlaybackStateCompat.State
    private int state = PlaybackStateCompat.STATE_NONE;

    public PlayPauseButton(Context context) { this(context, null); }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    public static PlayPauseButton create(Context context, OnClickListener listener) {
        PlayPauseButton toReturn = new PlayPauseButton(context);
        toReturn.setPlayIcon();
        toReturn.getView().setOnClickListener(listener);
        toReturn.init();
        return toReturn;
    }

    @Override
    public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
        updateState(state.getState());
    }
}
