package com.example.mike.mp3player.client.view;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.PlaybackStateWrapper;

public class PlayPauseButton extends AppCompatImageButton {

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

    public void updateState(PlaybackStateWrapper state) {
//        Log.d(LOG_TAG, Constants.playbackStateDebugMap.get(state.getPlaybackState().getState()) + ", " + state.getPlaybackState());
        updateState(state.getPlaybackState().getState());
    }

    public void updateState(int state) {
        switch (state) {
            case PlaybackStateCompat.STATE_PLAYING: setPauseIcon();   break;
            default: setPlayIcon(); break;
        } // switch

    }

    public void setPlayIcon() {
        this.setImageResource(R.drawable.ic_baseline_play_arrow_24px);
    }
    public void setPauseIcon() {
        this.setImageResource(R.drawable.ic_baseline_pause_24px);
    }


}
