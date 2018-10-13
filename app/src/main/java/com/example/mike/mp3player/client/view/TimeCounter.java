package com.example.mike.mp3player.client.view;

import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.TextView;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.mike.mp3player.client.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.TimerUtils.formatTime;

public class TimeCounter {
    private TextView view;
    private long duration;
    private long currentTime;
    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private boolean isRunning = false;
    public static final String LOG_TAG = "TimeCounter";

    public TimeCounter(TextView view) {
        this.view = view;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void updateState(PlaybackStateCompat state) {
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING: work(state.getPosition()); break;
            case PlaybackStateCompat.STATE_PAUSED: haltTimer(state.getPosition()); break;
            default: resetTimer(); break;
        }
    }

    private void work(long startTime) {
        currentTime = startTime;
        view.setText(formatTime(startTime));
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime += ONE_SECOND;
                view.setText(formatTime(currentTime));
            }
        };
        cancelTimer();
        timer.scheduleAtFixedRate(timerTask,0L, ONE_SECOND, TimeUnit.MILLISECONDS);
        isRunning = true;
    }

    private void haltTimer(long currentTime) {
        cancelTimer();
        this.currentTime = currentTime;
        view.setText(formatTime(currentTime));
    }

    private void resetTimer() {
        cancelTimer();
        this.currentTime = 0L;
        view.setText(formatTime(0L));
    }

    private void cancelTimer() {
        if (isRunning) {
            // cancel timer and make new one
            timer.shutdown();
            timer = Executors.newSingleThreadScheduledExecutor();
        }
        isRunning = false;

    }

}


