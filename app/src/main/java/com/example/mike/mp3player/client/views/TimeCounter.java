package com.example.mike.mp3player.client.views;

import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.client.utils.TimerUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.PlaybackStateUtil.getRepeatModeFromPlaybackStateCompat;

public class TimeCounter {
    private static final long START = 0L;
    public static final String LOG_TAG = "TimeCounter";
    private TextView textView;
    private long duration;
    private long currentPosition = 0;
    private int currentState = STATE_NONE;
    private float currentSpeed;
    private ScheduledExecutorService timer;
    private Handler mainHandler;
    private boolean repeating = false;

    @Inject
    public TimeCounter(@Named("main") Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public boolean isInitialised() {
        return textView != null;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void seekTo(long position) {
        this.currentPosition = position;
        updateTimerText();
    }

    public void cancelTimerDuringTracking() {
        //Log.d(LOG_TAG, "cancel timer during tracking");
        cancelTimer();
    }
    public void updateState(PlaybackStateCompat state) {
        //Log.d(LOG_TAG, "new state");
        this.currentState = state.getState();
        this.currentSpeed = state.getPlaybackSpeed();
        Integer repeatMode = getRepeatModeFromPlaybackStateCompat(state);
        this.repeating = repeatMode != null && repeatMode == REPEAT_MODE_ONE;
        long latestPosition = TimerUtils.calculateCurrentPlaybackPosition(state);

        if (isInitialised()) {
            switch (getCurrentState()) {
                case PlaybackStateCompat.STATE_PLAYING:
                    work(latestPosition);
                    break;
                case PlaybackStateCompat.STATE_PAUSED:
                    haltTimer(latestPosition);
                    break;
                default:
                    resetTimer();
                    break;
            }
        }
    }

    private void work(long startTime) {
        //Log.d(LOG_TAG, "work timer");
        this.currentPosition = startTime;
        updateTimerText();
        createTimer();
    }

    private void haltTimer(long currentTime) {
        Log.i(LOG_TAG, "halt timer");
        cancelTimer();
        this.currentPosition = currentTime;
        updateTimerText();
    }

    private void resetTimer() {
        //Log.d(LOG_TAG, "reset timer");
        cancelTimer();
        this.currentPosition = START;
        updateTimerText();
    }

    private void cancelTimer() {
        //Log.d(LOG_TAG, "Cancel timer");
        if (timer != null) {
            // cancel timer and make new one
            timer.shutdown();
        }
    }

    private void createTimer() {
        cancelTimer();
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::updateUi,0L, getTimerFixedRate(), TimeUnit.MILLISECONDS);
        //Log.d(LOG_TAG, "create timer");
    }

    private long getTimerFixedRate() {
        /**
         * e.g. slower playback speed => longer update time
         * 0.95 playbacks speed => 1000ms / 0.95 = 1052
         **/
        return (long)(ONE_SECOND / currentSpeed);
    }

    private void updateTimerText() {
        String text = formatTime(currentPosition);
        mainHandler.post(() -> { textView.setText(text);} );
    }
    
    private void updateUi() {
        //Log.d(LOG_TAG,"current position: " + timeCounter.getCurrentPosition() + ", duration: " + timeCounter.getDuration());
        final long newPosition = currentPosition + ONE_SECOND;
        if (newPosition < duration) {
            this.currentPosition = newPosition;
            this.updateTimerText();
        } else if (isRepeating()) {
            this.currentPosition = DEFAULT_POSITION;
            this.updateTimerText();
        }
        //Log.d(LOG_TAG, "finished run call");
    } // run

    public boolean isRunning() {
        return currentState == STATE_PLAYING;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCurrentState() {
        return currentState;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public void init(TextView textView) {
        this.textView = textView;
    }

    @VisibleForTesting
    public ScheduledExecutorService getTimer() {
        return timer;
    }

    @VisibleForTesting
    public void setTimer(ScheduledExecutorService timer) {
        this.timer = timer;
    }
}


