package com.example.mike.mp3player.client.callbacks;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.SeekerBar;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;

/**
 *
 */
public class SeekerBarController2 implements ValueAnimator.AnimatorUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final String LOG_TAG = "SKR_MDIA_CNTRLR_CLBK";
    private final SeekerBar seekerBar;
    private final int NO_PROGRESS = 0;
    @PlaybackStateCompat.State
    private int currentState = STATE_PAUSED;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private int currentPosition = DEFAULT_POSITION;
    private long currentSongDuration = 0;
    private boolean looping = false;
    private ValueAnimator valueAnimator = null;
    private final MediaControllerAdapter mediaControllerAdapter;

    public SeekerBarController2(SeekerBar seekerBar, MediaControllerAdapter mediaControllerAdapter) {
        this.seekerBar = seekerBar;
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        setLooping(state);
        this.currentState = state.getState();
        long position = state.getPosition();
        if (validPosition(position)) {
            this.currentPosition = (int) position;
        }

        float speed = state.getPlaybackSpeed();
        boolean speedChanged = speed != currentPlaybackSpeed;
        if (speedChanged) {
            this.currentPlaybackSpeed = speed;
        }

        if (null == valueAnimator || speedChanged) {
            Log.i(LOG_TAG, "recreate animator");
            createAnimator();
        }
        seekerBar.setProgress(currentPosition);
        if (valueAnimator.isStarted()) {
            valueAnimator.start();
        }
        switch (currentState) {
            case PlaybackStateCompat.STATE_PAUSED: valueAnimator.pause(); break;
            case PlaybackStateCompat.STATE_PLAYING: valueAnimator.resume(); break;
            default: break;
        }
        this.currentState = state.getState();
        this.currentPlaybackSpeed = state.getPlaybackSpeed();
    }

    /**
     *
     * @param metadata
     */
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        final int max = metadata != null
                ? (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                : 0;
        seekerBar.setMax(max);
        this.currentSongDuration = max;
        createAnimator();
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
        final int animatedIntValue = (int) valueAnimator.getAnimatedValue();
        seekerBar.setProgress(animatedIntValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        SeekerBar seekerBar = (SeekerBar) seekBar;
        seekerBar.getTimeCounter().cancelTimerDuringTracking();
        valueAnimator.cancel();
        setTracking(seekBar, true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, false);
        SeekerBar seekerBar = (SeekerBar) seekBar;
        if (seekerBar != null ) {
            int progress = seekBar.getProgress();
            mediaControllerAdapter.seekTo(progress);
            valueAnimator.start();
            valueAnimator.setCurrentPlayTime(progress);
        }
    }

    private void setTracking(SeekBar seekBar, boolean tracking) {
        if (seekBar instanceof  SeekerBar) {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(tracking);
        }
    }

    private void createAnimator() {
        removeValueAnimator();
        try {
            int duration = (int) (seekerBar.getMax() / currentPlaybackSpeed);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, duration);
            valueAnimator.setDuration(duration);
            valueAnimator.addUpdateListener(this);
            valueAnimator.setInterpolator(new LinearInterpolator());
            seekerBar.setValueAnimator(valueAnimator);
            this.valueAnimator = valueAnimator;
        } catch (IllegalArgumentException ex) {
            Log.e(getClass().getName(), "seekerbar Max: " + currentSongDuration);
            throw new IllegalArgumentException(ex);
        }
    }

    public void setLooping(PlaybackStateCompat state) {
        Bundle extras = state.getExtras();
        if (null != extras) {
            Integer repeatMode = extras.getInt(REPEAT_MODE);
            if (null != repeatMode) {
                this.looping = repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE;
            }
        }
    }
    private void removeValueAnimator() {
        if (null != seekerBar && null != seekerBar.getValueAnimator()) {
            seekerBar.getValueAnimator().removeAllUpdateListeners();
            seekerBar.getValueAnimator().cancel();
            seekerBar.setValueAnimator(null);
        }
    }

    private boolean validPosition(long position) {
        return position >= 0 && position <= seekerBar.getMax();
    }

    public boolean isLooping() {
        return looping;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
   //     Log.i(LOG_TAG, "hit progressed changed");
        seekerBar.setProgress(progress);
    }
}