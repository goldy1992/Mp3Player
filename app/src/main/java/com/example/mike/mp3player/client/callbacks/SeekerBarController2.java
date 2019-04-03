package com.example.mike.mp3player.client.callbacks;

import android.animation.ValueAnimator;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.SeekerBar;
import com.example.mike.mp3player.commons.LoggingUtils;

import androidx.annotation.VisibleForTesting;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;
import static com.example.mike.mp3player.commons.PlaybackStateUtil.getRepeatModeFromPlaybackStateCompat;

/**
 *
 */
public class SeekerBarController2 implements ValueAnimator.AnimatorUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final String LOG_TAG = "SKR_MDIA_CNTRLR_CLBK";
    private final SeekerBar seekerBar;
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
        LoggingUtils.logPlaybackStateCompat(state, LOG_TAG);
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
        updateValueAnimator();
    }

    /**
     *
     * @param metadata
     */
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        Log.i(LOG_TAG, "meta data change");
        final int max = metadata != null
                ? (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                : 0;
        seekerBar.setMax(max);
        this.currentSongDuration = max;
        createAnimator();
        updateValueAnimator();
    }

    private void updateValueAnimator() {
        valueAnimator.setCurrentFraction(getPositionAsFraction());
        if (!valueAnimator.isStarted()) {
            valueAnimator.start();
        }
        switch (currentState) {
            case PlaybackStateCompat.STATE_PAUSED:
                valueAnimator.pause();
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                valueAnimator.resume();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
        //Log.i(LOG_TAG, "animation update");
        final int animatedIntValue = (int) valueAnimator.getAnimatedValue();
        seekerBar.setProgress(animatedIntValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Log.i(LOG_TAG, "START TRACKING");
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
            this.currentPosition = seekBar.getProgress();
            mediaControllerAdapter.seekTo(this.currentPosition);
            valueAnimator.start();
            valueAnimator.setCurrentFraction(getPositionAsFraction());
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
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, seekerBar.getMax());
            valueAnimator.setDuration(duration);
            valueAnimator.addUpdateListener(this);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(isLooping() ? ValueAnimator.INFINITE : 0);

            if (currentPosition >= 0 && seekerBar.getMax() > currentPosition) {
                valueAnimator.setCurrentFraction(getPositionAsFraction());
            }
            seekerBar.setValueAnimator(valueAnimator);
            this.valueAnimator = valueAnimator;
        } catch (IllegalArgumentException ex) {
            Log.e(getClass().getName(), "seekerbar Max: " + currentSongDuration);
            throw new IllegalArgumentException(ex);
        }
    }

    public void setLooping(PlaybackStateCompat state) {
        Integer repeatMode = getRepeatModeFromPlaybackStateCompat(state);
        if (null != repeatMode) {
            this.looping = repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE;
            if (null != valueAnimator) {
                valueAnimator.setRepeatCount(isLooping() ? ValueAnimator.INFINITE : 0);
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

    private float getPositionAsFraction() {
        return currentPosition / (float) seekerBar.getMax();
    }

    public boolean isLooping() {
        return looping;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar != null) {
            if (seekBar instanceof SeekerBar) {
                SeekerBar seekerBar = (SeekerBar) seekBar;
                if (seekerBar.isTracking()) {
                    seekerBar.setTimerCounterProgress(progress);
                }
            }
        }
    }

    @VisibleForTesting
    public ValueAnimator getValueAnimator() {
        return this.valueAnimator;
    }
}