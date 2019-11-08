package com.github.goldy1992.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.github.goldy1992.mp3player.commons.Constants.OPAQUE;
import static com.github.goldy1992.mp3player.commons.Constants.TRANSLUCENT;

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

    @Override
    public void init(ImageView view) {
        super.init(view);
        this.mediaControllerAdapter.registerPlaybackStateListener(this);
        this.updateState(mediaControllerAdapter.getRepeatMode());
    }

    @VisibleForTesting
    public void onClick(View view) {
        int nextState = getNextState();
        mediaControllerAdapter.setRepeatMode(nextState);
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
        setImage(R.drawable.ic_baseline_repeat_24px, OPAQUE);
    }
    private void setRepeatOneIcon() {
        setImage(R.drawable.ic_baseline_repeat_one_24px, OPAQUE);
    }
    private void setRepeatNoneIcon() {
        setImage(R.drawable.ic_baseline_repeat_24px, TRANSLUCENT);
    }


    public int getState() {
        return repeatMode;
    }

    @VisibleForTesting
    public void setRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
        this.repeatMode = repeatMode;
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
        final int newRepeatMode = mediaControllerAdapter.getRepeatMode();
        if (this.repeatMode != newRepeatMode) {
            updateState(newRepeatMode);
        }
    }
}
