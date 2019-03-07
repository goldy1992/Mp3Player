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
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;


public class SeekerBarController extends MediaControllerCompat.Callback implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final String LOG_TAG = "SKR_MDIA_CNTRLR_CLBK";
    private final SeekerBar seekerBar;
    private final int NO_PROGRESS = 0;
    @PlaybackStateCompat.State
    private int currentState = STATE_PAUSED;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private long currentSongDuration = 0;
    private boolean looping = false;
    private final MediaControllerAdapter mediaControllerAdapter;

    public SeekerBarController(SeekerBar seekerBar, MediaControllerAdapter mediaControllerAdapter) {
        this.seekerBar = seekerBar;
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Override
    public void onSessionDestroyed() {
        super.onSessionDestroyed();
    }

    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        setLooping(state);
        this.currentState = state.getState();
        this.currentPlaybackSpeed = state.getPlaybackSpeed();
        long latestPosition = TimerUtils.calculateCurrentPlaybackPosition(state);
        createAndStartAnimator(latestPosition);
    }


    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        super.onMetadataChanged(metadata);
        final int max = metadata != null
                ? (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                : 0;
        seekerBar.setMax(max);
        this.currentSongDuration = max;
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
        setTracking(seekBar, true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, false);
        SeekerBar seekerBar = (SeekerBar) seekBar;
        if (seekerBar != null ) {
            mediaControllerAdapter.seekTo(seekBar.getProgress());
            if (seekerBar.getValueAnimator() != null) {
                seekerBar.getValueAnimator().cancel();
            }

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
        if (isLooping()) {
            createAndStartAnimator(0);
        } else {
            seekerBar.setProgress(0);
            if (seekerBar.getValueAnimator() != null ) {
                seekerBar.getValueAnimator().cancel();
            }
            seekerBar.setValueAnimator(null);
        }
    }

    private void createAndStartAnimator(long latestPosition) {

        if (seekerBar.getValueAnimator() != null) {
            seekerBar.getValueAnimator().cancel();
            seekerBar.setValueAnimator(null);
        }

        final int progress = validPosition(latestPosition) ? (int) latestPosition : NO_PROGRESS;
        seekerBar.setProgress(progress);

        // If the media is playing then the seekbar should follow it, and the easiest
        // way to do that is to create a ValueAnimator to update it so the bar reaches
        // the end of the media the same time as playback gets there (or close enough).
        if (currentState == PlaybackStateCompat.STATE_PLAYING) {
            final int timeToEnd = (int) ((currentSongDuration - progress) / currentPlaybackSpeed);

            try {
                seekerBar.setValueAnimator(ValueAnimator.ofInt(progress, seekerBar.getMax())
                        .setDuration(timeToEnd));
                seekerBar.getValueAnimator().addListener(this);

            } catch (IllegalArgumentException ex) {
                Log.e(getClass().getName(), "progress: " + progress + ", seekerbarMax: " + currentSongDuration);
                throw new IllegalArgumentException(ex);
            }
            seekerBar.getValueAnimator().setInterpolator(new LinearInterpolator());
            seekerBar.getValueAnimator().addUpdateListener(this);
            seekerBar.getValueAnimator().start();
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
}