package com.example.mike.mp3player.client.callbacks;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.SeekerBar;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;


public class SeekerBarController2 extends MediaControllerCompat.Callback implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final String LOG_TAG = "SKR_MDIA_CNTRLR_CLBK";
    private final SeekerBar seekerBar;
    private final int NO_PROGRESS = 0;
    @PlaybackStateCompat.State
    private int currentState = STATE_PAUSED;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private long currentSongDuration = 0;
    private boolean looping = false;
    private ValueAnimator valueAnimator= null
    private final MediaControllerAdapter mediaControllerAdapter;

    public SeekerBarController2(SeekerBar seekerBar, MediaControllerAdapter mediaControllerAdapter) {
        this.seekerBar = seekerBar;
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Override
    public void onSessionDestroyed() {
        super.onSessionDestroyed();
    }

    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        setLooping(state);
        if (null != valueAnimator) {
            createAnimator();
        }

        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {

        }
        this.currentState = state.getState();
        this.currentPlaybackSpeed = state.getPlaybackSpeed();
        long latestPosition = TimerUtils.calculateCurrentPlaybackPosition(state);
        // createAndStartAnimator(latestPosition);
    }

    /**
     *
     * @param metadata
     */
    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        super.onMetadataChanged(metadata);
        final int max = metadata != null
                ? (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                : 0;
        seekerBar.setMax(max);
        this.currentSongDuration = max;
        createAndStartAnimator(DEFAULT_POSITION);
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
        removeValueAnimator();
        setTracking(seekBar, true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, false);
        SeekerBar seekerBar = (SeekerBar) seekBar;
        if (seekerBar != null ) {
            mediaControllerAdapter.seekTo(seekBar.getProgress());
        }
    }

    private void setTracking(SeekBar seekBar, boolean tracking) {
        if (seekBar instanceof  SeekerBar) {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(tracking);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
//        if (seekerBar != null && !seekerBar.isTracking()) {
//            if (isLooping()) {
//                createAndStartAnimator(DEFAULT_POSITION);
//            } else {
//                removeValueAnimator();
//            }
//            seekerBar.setProgress(DEFAULT_POSITION);
//        }
    }

    private void createAnimator() {
        removeValueAnimator();
        try {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, seekerBar.getMax());
            int duration = (int) (seekerBar.getMax() * currentPlaybackSpeed);
            valueAnimator.setDuration(duration);
            valueAnimator.addListener(this);
            valueAnimator.setInterpolator(new LinearInterpolator());
            seekerBar.setValueAnimator(valueAnimator);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                seekerBar.getValueAnimator().start();
            }
        } catch (IllegalArgumentException ex) {
            Log.e(getClass().getName(), "seekerbar Max: " + currentSongDuration);
            throw new IllegalArgumentException(ex);
        }
    }


    private boolean validPosition(long position) {
        return position >= 0 && position <= seekerBar.getMax();
    }

    @Override
    public void onAnimationCancel(Animator animation) { }
    @Override
    public void onAnimationRepeat(Animator animation) { }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
    @Override
    public void onAnimationStart(Animator animation) {  }

    public boolean isLooping() {
        return looping;
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
}