package com.example.mike.mp3player.client.views;

import android.graphics.drawable.Drawable;
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

public class PlayPauseButton implements PlaybackStateListener {

    private static final String LOG_TAG = "PLAY_PAUSE_BUTTON";
    @PlaybackStateCompat.State
    private int state = PlaybackStateCompat.STATE_NONE;
    private final MediaControllerAdapter mediaControllerAdapter;
    private final Handler mainUpdater;

    private ImageView view;


    @Inject
    public PlayPauseButton(@NonNull MediaControllerAdapter mediaControllerAdapter,
                           @Named("main") Handler mainUpdater) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mainUpdater = mainUpdater;
    }

    public void init(ImageView view) {
        this.view = view;
//        this.view.init(getCurrentIcon(), 2);
//        this.view.setLayoutParams(view.getLinearLayoutParams(view.getLayoutParams()));
        this.view.setOnClickListener(this::playPause);
        this.mediaControllerAdapter.registerPlaybackStateListener(this,
                Collections.singleton(ListenerType.PLAYBACK));
        this.updateState(mediaControllerAdapter.getPlaybackState());
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

    private @DrawableRes int getCurrentIcon() {
        return mediaControllerAdapter.getPlaybackState() == STATE_PLAYING ?
            getPauseIcon() : getPlayIcon();
    }

    private void updateState(int newState) {
        if (newState != getState()) {
            switch (newState) {
                case STATE_PLAYING:
                    mainUpdater.post(this::setPauseIcon);
                    this.state = STATE_PLAYING;
                    break;
                default:
                    mainUpdater.post(this::setPlayIcon);
                    this.state = STATE_PAUSED;
            } // switch
        }
    }

    private synchronized void setPlayIcon() {
        int resource = getPlayIcon();
        Drawable drawable = mediaControllerAdapter.getContext().getDrawable(resource);
        view.setImageDrawable(drawable);
    }
    private synchronized void setPauseIcon() {
        int resource = getPauseIcon();
        Drawable drawable = mediaControllerAdapter.getContext().getDrawable(resource);
        view.setImageDrawable(drawable);
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
