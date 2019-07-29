package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import javax.inject.Inject;

import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE;
import static com.example.mike.mp3player.commons.Constants.OPAQUE;
import static com.example.mike.mp3player.commons.Constants.SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.Constants.TRANSLUCENT;

public class ShuffleButton extends LinearLayoutWithImageView implements PlaybackStateListener {

    private static final String LOG_TAG = "SHUFFLE_BTN";
    private Context context;
    @PlaybackStateCompat.ShuffleMode
    private int shuffleMode;
    private MediaControllerAdapter mediaControllerAdapter;

    public ShuffleButton(Context context) {
        this(context, null);
    }

    public ShuffleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShuffleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0, 1, R.drawable.ic_baseline_shuffle_24px);
        this.context = context;
    }

    public void init(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.setOnClickListener(this::toggleShuffle);
        this.updateState(mediaControllerAdapter.getShuffleMode());
    }

    public void updateState(@PlaybackStateCompat.ShuffleMode int newState) {
        switch (newState) {
            case SHUFFLE_MODE_ALL:
                this.shuffleMode = SHUFFLE_MODE_ALL;
                mainUpdater.post(this::setShuffleOn);
                break;
            default:
                this.shuffleMode = SHUFFLE_MODE_NONE;
                mainUpdater.post(this::setShuffleOff);
                break;
        }
    }

    public @PlaybackStateCompat.ShuffleMode int toggleShuffle(View view) {
        @PlaybackStateCompat.ShuffleMode int newShuffleMode;
        switch (shuffleMode) {
            case SHUFFLE_MODE_ALL: newShuffleMode = SHUFFLE_MODE_NONE; break;
            default: newShuffleMode = SHUFFLE_MODE_ALL; break;
        }
        updateState(newShuffleMode);
        mediaControllerAdapter.setShuffleMode(newShuffleMode);
        return shuffleMode;
    }

    private void setShuffleOn() {
        setViewImage(R.drawable.ic_baseline_shuffle_24px);
        getView().setImageAlpha(OPAQUE);
    }
    private void setShuffleOff() {
        setViewImage(R.drawable.ic_baseline_shuffle_24px);
        getView().setImageAlpha(TRANSLUCENT);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        Bundle extras = state.getExtras();
        if (null != extras) {
            Integer shuffleMode = extras.getInt(SHUFFLE_MODE);
            if (null != shuffleMode) {
                updateState(shuffleMode);
            }
        }
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @VisibleForTesting
    public int getShuffleMode() {
        return shuffleMode;
    }

    @VisibleForTesting
    public void setShuffleMode(int shuffleMode) {
        this.shuffleMode = shuffleMode;
    }
}