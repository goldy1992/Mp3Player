package com.example.mike.mp3player.client.views;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.TextView;

import com.example.mike.mp3player.client.TimeCounterTimerTask;
import com.example.mike.mp3player.client.utils.TimerUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;
import static com.example.mike.mp3player.commons.PlaybackStateUtil.getRepeatModeFromPlaybackStateCompat;

public class TimeCounter {
    public static final String LOG_TAG = "TimeCounter";
    private TextView view;
    private long duration;
    private long currentPosition;
    private int currentState;
    private float currentSpeed;
    private ScheduledExecutorService timer;
    private boolean isRunning = false;
    private Handler mainHandler;
    private boolean repeating = false;

    public TimeCounter(TextView view) {
        this.view = view;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void seekTo(long position) {
        this.currentPosition = position;
        setTimerText(formatTime(position));
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

        switch (getCurrentState()) {
            case PlaybackStateCompat.STATE_PLAYING: work(latestPosition); break;
            case PlaybackStateCompat.STATE_PAUSED: haltTimer(latestPosition); break;
            default: resetTimer(); break;
        }
    }

    private void work(long startTime) {
        //Log.d(LOG_TAG, "work timer");
        setCurrentPosition(startTime);
        setTimerText(formatTime(startTime));
        createTimer();
        setRunning(true);
    }

    private void haltTimer(long currentTime) {
        //Log.d(LOG_TAG, "halt timer");
        cancelTimer();
        this.setCurrentPosition(currentTime);
        setTimerText(formatTime(currentTime));
    }

    private void resetTimer() {
        //Log.d(LOG_TAG, "reset timer");
        cancelTimer();
        this.setCurrentPosition(0L);
        setTimerText(formatTime(0L));
    }

    private void cancelTimer() {
        Log.d(LOG_TAG, "Cancel timer");
        if (timer != null && isRunning()) {
            // cancel timer and make new one
            timer.shutdown();

        }
        setRunning(false);

    }

    private void createTimer() {
        TimeCounterTimerTask timerTask = new TimeCounterTimerTask(this, mainHandler);
        cancelTimer();
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(timerTask,0L, getTimerFixedRate(), TimeUnit.MILLISECONDS);
        //Log.d(LOG_TAG, "create timer");
    }

    private long getTimerFixedRate() {
        /**
         * e.g. slower playback speed => longer update time
         * 0.95 playbacks speed => 1000ms / 0.95 = 1052
         **/
        return (long)(ONE_SECOND / currentSpeed);
    }

    private void setTimerText(String text) {
        mainHandler.post(() -> {getView().setText(text);});
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public TextView getView() {
        return view;
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
}


