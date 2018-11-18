package com.example.mike.mp3player.client.view;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

import com.example.mike.mp3player.commons.Constants;

public class PlayPauseButton extends AppCompatButton {

    private final String PLAY = "Play";
    private final String PAUSE = "Pause";
    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";

    public PlayPauseButton(Context context) {
        super(context);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateState(PlaybackStateCompat state) {
        Log.d(LOG_TAG, Constants.playbackStateDebugMap.get(state.getState()) + ", " + state.getPlaybackState());
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING: setTextPause(); break;
            default: setTextPlay(); break;
        } // switch
    }

    public void setTextPlay() {
        this.setText(PLAY);
    }

    public void setTextPause() {
        this.setText(PAUSE);
    }
}
