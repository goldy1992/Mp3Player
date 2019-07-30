package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE;
import static com.example.mike.mp3player.commons.Constants.OPAQUE;
import static com.example.mike.mp3player.commons.Constants.SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.Constants.TRANSLUCENT;

public class ShuffleButton implements PlaybackStateListener {

    private static final String LOG_TAG = "SHUFFLE_BTN";
    @PlaybackStateCompat.ShuffleMode
    private int shuffleMode;
    private final Context context;
    private final MediaControllerAdapter mediaControllerAdapter;
    private final Handler mainUpdater;
    private ImageView view;

    @Inject
    public ShuffleButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                         @Named("main") Handler mainUpdater) {
        this.context = context;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mainUpdater = mainUpdater;
    }

    public void init(ImageView view) {
        this.view = view;
        this.view.setOnClickListener(this::toggleShuffle);
        this.mediaControllerAdapter.registerPlaybackStateListener(this,
                Collections.singleton(ListenerType.SHUFFLE));
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
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_shuffle_24px);
        this.view.setImageDrawable(drawable);
        view.setImageAlpha(OPAQUE);
    }
    private void setShuffleOff() {
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_shuffle_24px);
        this.view.setImageDrawable(drawable);
        view.setImageAlpha(TRANSLUCENT);
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

    @VisibleForTesting
    public int getShuffleMode() {
        return shuffleMode;
    }

    @VisibleForTesting
    public void setShuffleMode(int shuffleMode) {
        this.shuffleMode = shuffleMode;
    }
}