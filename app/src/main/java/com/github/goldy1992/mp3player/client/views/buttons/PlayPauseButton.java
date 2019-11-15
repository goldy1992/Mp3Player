package com.github.goldy1992.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;

public class PlayPauseButton extends MediaButton implements PlaybackStateListener {

    @PlaybackStateCompat.State
    public static final int INITIAL_PLAYBACK_STATE = PlaybackStateCompat.STATE_STOPPED;

    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";
    @PlaybackStateCompat.State
    private int state = INITIAL_PLAYBACK_STATE;

    @Inject
    public PlayPauseButton(Context context, @NonNull MediaControllerAdapter mediaControllerAdapter,
                           @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);
    }

    @Override
    public void init(ImageView view) {
        super.init(view);
        updateState(this.mediaControllerAdapter.getPlaybackState());
        this.mediaControllerAdapter.registerPlaybackStateListener(this);
    }

    @VisibleForTesting()
    @Override
    public void onClick(View view) {
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
                    setStatePlaying();
                    break;
                default:
                    setStatePaused();
            } // switch
        }
    }

    private void setStatePlaying() {
        mainUpdater.post(this::setPauseIcon);
        this.state = STATE_PLAYING;
    }

    private void setStatePaused() {
        mainUpdater.post(this::setPlayIcon);
        this.state = STATE_PAUSED;
    }

    private synchronized void setPlayIcon() {
        setImage(getPlayIcon());
    }
    private synchronized void setPauseIcon() {
        setImage(getPauseIcon());
    }

    private @DrawableRes int getPlayIcon() {
        return R.drawable.ic_baseline_play_arrow_24px;
    }

    private @DrawableRes int getPauseIcon() {
        return R.drawable.ic_baseline_pause_24px;
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
