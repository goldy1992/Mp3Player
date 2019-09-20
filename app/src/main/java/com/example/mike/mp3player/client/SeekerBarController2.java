package com.example.mike.mp3player.client;

import android.animation.ValueAnimator;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.client.views.SeekerBar;
import com.example.mike.mp3player.client.views.TimeCounter;
import com.example.mike.mp3player.commons.LoggingUtils;

import javax.inject.Inject;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;

/**
 *
 */
public class SeekerBarController2 implements ValueAnimator.AnimatorUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final String LOG_TAG = "SKR_MDIA_CNTRLR_CLBK";
    private SeekerBar seekerBar;
    @PlaybackStateCompat.State
    private int currentState = STATE_PAUSED;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private int currentPosition = DEFAULT_POSITION;
    private long currentSongDuration = 0;
    private boolean looping = false;
    private ValueAnimator valueAnimator = null;
    private final MediaControllerAdapter mediaControllerAdapter;
    private final TimeCounter timeCounter;

    @Inject
    public SeekerBarController2(MediaControllerAdapter mediaControllerAdapter, TimeCounter timeCounter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.timeCounter = timeCounter;
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

        createAnimator();
        updateValueAnimator();
    }

    /**
     *
     * @param metadata the metadata object
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
        float newPosition = getPositionAsFraction();
        Log.d(LOG_TAG, "new fraction: " + newPosition);
        valueAnimator.setCurrentFraction(newPosition);
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
   //     Log.i(LOG_TAG, "animation update from: " + valueAnimator);
        final int animatedIntValue = (int) valueAnimator.getAnimatedValue();
        seekerBar.setProgress(animatedIntValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(LOG_TAG, "START TRACKING");
        timeCounter.cancelTimerDuringTracking();
        removeValueAnimator();
        setTracking(seekBar, true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, false);
        Log.i(LOG_TAG, "Stop TRACKING");
        SeekerBar seekerBar = (SeekerBar) seekBar;
        if (seekerBar != null ) {
            this.currentPosition = seekBar.getProgress();
            mediaControllerAdapter.seekTo(this.currentPosition);
            createAnimator();
        }
    }

    private void setTracking(SeekBar seekBar, boolean tracking) {
        if (seekBar instanceof  SeekerBar) {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(tracking);
        }
    }

    private void createAnimator() {
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
            removeValueAnimator();
            seekerBar.setValueAnimator(valueAnimator);
            this.valueAnimator = valueAnimator;
        } catch (IllegalArgumentException ex) {
            Log.e(LOG_TAG, "seekerbar Max: " + currentSongDuration);
            throw new IllegalArgumentException(ex);
        }
    }

    public void setLooping(PlaybackStateCompat state) {
        Integer repeatMode = mediaControllerAdapter.getRepeatMode();
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

    /**
     * @param position the position to be compared
     * @return true if the position is greater than or equal to zero and less than or equal to the
     * maximum value of the current seeker bar
     */
    private boolean validPosition(long position) {
        boolean valid = position >= 0 && position <= seekerBar.getMax();
        Log.i(LOG_TAG, "position: " + position + ", valid: " + valid);
        return valid;
    }

    private float getPositionAsFraction() {
        Log.e(LOG_TAG, "current pos: " + currentPosition + ", seekerbar max" + seekerBar.getMax());
        return currentPosition / (float) seekerBar.getMax();
    }

    public boolean isLooping() {
        return looping;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        boolean updateTimer = seekBar != null && seekBar instanceof SeekerBar && ((SeekerBar) seekBar).isTracking();
        if (updateTimer) {
            Log.i(LOG_TAG, "PROGRESS CHANGED");
            timeCounter.seekTo(progress);
        }
    }

    /**
     *
     * @return true if the controller has been initialised correctly
     */
    public boolean isInitialised() {
        return mediaControllerAdapter != null && mediaControllerAdapter.isInitialized() &&
                seekerBar != null;
    }

    @VisibleForTesting
    public ValueAnimator getValueAnimator() {
        return this.valueAnimator;
    }

    /**
     * setter method automatically associates the on seeker bar change listener to be the controller
     * and therefore cannot be null
     * @param seekerBar the seeker bar
     */
    public void init(@NonNull SeekerBar seekerBar) {
        this.seekerBar = seekerBar;
        this.seekerBar.setOnSeekBarChangeListener(this);
    }
}