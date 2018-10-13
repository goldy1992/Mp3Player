package com.example.mike.mp3player.client.view;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.TextView;
import com.example.mike.mp3player.client.TimerUtils;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.mike.mp3player.client.TimerUtils.ONE_THOUSAND;
import static com.example.mike.mp3player.client.TimerUtils.formatTime;

public class TimeCounter {
    private TextView view;
    private long duration;
    private long currentTime;
    private Timer timer = new Timer();
    private boolean isRunning = false;
    private static final String LOG_TAG = "TimeCounter";

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
        cancelTimer();
        currentTime = startTime;
        Log.d(LOG_TAG, "duration " + duration + " - currentTime " + currentTime + " = " + (duration-currentTime));
        view.setText(formatTime(startTime));
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime += ONE_THOUSAND;
                Log.d(LOG_TAG, "current time: " + currentTime);
                view.setText(formatTime(currentTime));
            }
        };
        timer.scheduleAtFixedRate(timerTask,0L, ONE_THOUSAND);
        timerTask.run();
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
            timer.cancel();
        }
        isRunning = false;

    }

}


