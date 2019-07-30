package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.example.mike.mp3player.commons.Constants.OPAQUE;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static com.example.mike.mp3player.commons.Constants.TRANSLUCENT;

/**
 *
 */
public class RepeatOneRepeatAllButton extends MediaButton implements PlaybackStateListener {

    private static final String LOG_TAG = "RPT1_RPT_ALL_BTN";

    @PlaybackStateCompat.RepeatMode
    private int repeatMode;

    @Inject
    public RepeatOneRepeatAllButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                                    @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);

    }

    public void init(ImageView view) {
        this.view = view;
        this.view.setOnClickListener(this::setRepeatOneRepeatAllButtonMode);
        this.mediaControllerAdapter.registerPlaybackStateListener(this,
                Collections.singleton(ListenerType.REPEAT));
        this.updateState(mediaControllerAdapter.getRepeatMode());
    }

    private void setRepeatOneRepeatAllButtonMode(View view) {
        int nextState = getNextState();
        mediaControllerAdapter.setRepeatMode(nextState);
        updateState(mediaControllerAdapter.getRepeatMode());
        updateState(nextState);
    }

    public void updateState(@PlaybackStateCompat.RepeatMode int newState) {
        switch (newState) {
            case REPEAT_MODE_ALL: this.mainUpdater.post(this::setRepeatAllIcon);
                this.repeatMode = REPEAT_MODE_ALL;
                break;
            case REPEAT_MODE_ONE: this.mainUpdater.post(this::setRepeatOneIcon);
                this.repeatMode = REPEAT_MODE_ONE;
                break;
            case REPEAT_MODE_NONE: this.mainUpdater.post(this::setRepeatNoneIcon);
                this.repeatMode = REPEAT_MODE_NONE;
                break;
            default:
                StringBuilder sb = new StringBuilder().append("Invalid RepeatMode param: ").append(newState);
                Log.e(LOG_TAG, sb.toString());
        }
    }

    private void setRepeatAllIcon() {
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_repeat_24px);
        this.view.setImageDrawable(drawable);
        view.setImageAlpha(OPAQUE);
    }
    private void setRepeatOneIcon() {
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_repeat_one_24px);
        this.view.setImageDrawable(drawable);
        view.setImageAlpha(OPAQUE);
    }
    private void setRepeatNoneIcon() {
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_repeat_24px);
        this.view.setImageDrawable(drawable);
        view.setImageAlpha(TRANSLUCENT);
    }

    public int getState() {
        return repeatMode;
    }

    private int getNextState() {
        switch (repeatMode) {
            case REPEAT_MODE_ALL:
                return REPEAT_MODE_NONE;
            case REPEAT_MODE_NONE:
                return REPEAT_MODE_ONE;
            case REPEAT_MODE_ONE:
                return REPEAT_MODE_ALL;
            default: return REPEAT_MODE_NONE;
        }
    }

    @Override
    public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
        Bundle extras = state.getExtras();
        if (null != extras) {
            updateState(extras.getInt(REPEAT_MODE));
        }
    }
}
