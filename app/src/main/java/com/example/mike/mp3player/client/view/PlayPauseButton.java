package com.example.mike.mp3player.client.view;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.ViewGroupCompat;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;

public class PlayPauseButton extends LinearLayout {

    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";
    @PlaybackStateCompat.State
    private int state = PlaybackStateCompat.STATE_PAUSED;
    private Context context;
    private AppCompatImageButton button;

    public PlayPauseButton(Context context) {
        this(context, null);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init() {

        View.inflate(context, R.layout.button_play_pause, this);
        this.button = (AppCompatImageButton) this.findViewById(R.id.playPauseButton);
    }

    public void updateState(PlaybackStateCompat state) {
        updateState(state.getState());
    }

    public void updateState(int newState) {
        if (newState != getState()) {
            switch (newState) {
                case STATE_PLAYING:
                    setPauseIcon();
                    this.state = STATE_PLAYING;
                    break;
                default:
                    setPlayIcon();
                    this.state = STATE_PAUSED;
                    break;
            } // switch
        }

    }

    public void setPlayIcon() {
        this.setImageResource(R.drawable.ic_baseline_play_arrow_24px);
    }
    public void setPauseIcon() {
        this.setImageResource(R.drawable.ic_baseline_pause_24px);
    }

    @PlaybackStateCompat.State
    public int getState() {
        return state;
    }
}
