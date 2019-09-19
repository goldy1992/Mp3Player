package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;

public class PlayPauseButton extends MediaButton implements PlaybackStateListener {

    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";
    @PlaybackStateCompat.State
    private int state = PlaybackStateCompat.STATE_NONE;

    @Inject
    public PlayPauseButton(Context context, @NonNull MediaControllerAdapter mediaControllerAdapter,
                           @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);
        updateState(mediaControllerAdapter.getPlaybackState())  ;
    }

    @Override
    public void init(ImageView view) {
        super.init(view);
        this.mediaControllerAdapter.registerPlaybackStateListener(this,
                Collections.singleton(ListenerType.PLAYBACK));
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
