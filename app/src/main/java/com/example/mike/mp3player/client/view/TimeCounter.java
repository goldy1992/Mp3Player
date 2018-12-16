package com.example.mike.mp3player.client.view;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.TextView;

import com.example.mike.mp3player.client.PlaybackStateWrapper;
import com.example.mike.mp3player.client.TimeCounterTimerTask;
import com.example.mike.mp3player.client.utils.TimerUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;

public class TimeCounter {
    private TextView view;
    private long duration;
    private long currentTime;
    private int currentState;
    private float currentSpeed;
    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private boolean isRunning = false;
    public static final String LOG_TAG = "TimeCounter";

    public TimeCounter(TextView view) {
        this.view = view;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void cancelTimerDuringTracking() {
        cancelTimer();
    }
    public void updateState(PlaybackStateWrapper state) {
        this.currentState = state.getPlaybackState().getState();
        this.currentSpeed = state.getPlaybackState().getPlaybackSpeed();
        long latestPosition = TimerUtils.calculateCurrentPlaybackPosition(state);

        switch (getCurrentState()) {
            case PlaybackStateCompat.STATE_PLAYING: work(latestPosition); break;
            case PlaybackStateCompat.STATE_PAUSED: haltTimer(latestPosition); break;
            default: resetTimer(); break;
        }
    }

    private void work(long startTime) {
        setCurrentTime(startTime);
        getView().setText(formatTime(startTime));
        TimeCounterTimerTask timerTask = new TimeCounterTimerTask(this);
        cancelTimer();
        timer.scheduleAtFixedRate(timerTask,0L, getTimerFixedRate(), TimeUnit.MILLISECONDS);
        setRunning(true);
    }

    private void haltTimer(long currentTime) {
        cancelTimer();
        this.setCurrentTime(currentTime);
        getView().setText(formatTime(currentTime));
    }

    private void resetTimer() {
        cancelTimer();
        this.setCurrentTime(0L);
        getView().setText(formatTime(0L));
    }

    private void cancelTimer() {
        if (isRunning()) {
            // cancel timer and make new one
            timer.shutdown();
            timer = Executors.newSingleThreadScheduledExecutor();
        }
        setRunning(false);

    }

    private long getTimerFixedRate() {
        /**
         * e.g. slower playback speed => longer update time
         * 0.95 playbacks speed => 1000ms / 0.95 = 1052
         **/
        Log.d("", "current speed:" + currentSpeed);
        Log.d("", "new speed:" + (long)(ONE_SECOND / currentSpeed));
        return (long)(ONE_SECOND / currentSpeed);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
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
}


