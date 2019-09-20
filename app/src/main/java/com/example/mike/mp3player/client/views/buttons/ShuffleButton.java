package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE;
import static com.example.mike.mp3player.commons.Constants.OPAQUE;
import static com.example.mike.mp3player.commons.Constants.TRANSLUCENT;

public class ShuffleButton extends MediaButton implements PlaybackStateListener {

    private static final String LOG_TAG = "SHUFFLE_BTN";
    @PlaybackStateCompat.ShuffleMode
    private int shuffleMode;

    @Inject
    public ShuffleButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                         @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);
    }

    @Override
    public void init(ImageView view) {
        super.init(view);
        this.mediaControllerAdapter.registerPlaybackStateListener(this);
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

    @Override
    public void onClick(View view) {
        @PlaybackStateCompat.ShuffleMode int newShuffleMode;
        switch (shuffleMode) {
            case SHUFFLE_MODE_ALL: newShuffleMode = SHUFFLE_MODE_NONE; break;
            default: newShuffleMode = SHUFFLE_MODE_ALL; break;
        }
        mediaControllerAdapter.setShuffleMode(newShuffleMode);
    }

    private void setShuffleOn() {
        setImage(R.drawable.ic_baseline_shuffle_24px, OPAQUE);
    }

    private void setShuffleOff() {
        setImage(R.drawable.ic_baseline_shuffle_24px, TRANSLUCENT);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        final int newShuffleMode = mediaControllerAdapter.getShuffleMode();
        if (this.shuffleMode != newShuffleMode) {
            updateState(newShuffleMode);
        }
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