package com.example.mike.mp3player.client.view;

import android.support.v4.media.session.PlaybackStateCompat;
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
    private Timer timer;

    public TimeCounter(TextView view) {
        this.view = view;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void updateState(PlaybackStateCompat state) {
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING: work(state.getPosition());
            case PlaybackStateCompat.STATE_PAUSED: haltTimer(state.getPosition());
            default: resetTimer();
        }
    }

    private void work(long startTime) {
        currentTime = startTime;
        view.setText(formatTime(startTime));

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime += ONE_THOUSAND;
                view.setText(formatTime(currentTime));
            }
        };
        timer.scheduleAtFixedRate(timerTask,(long) ONE_THOUSAND, currentTime-startTime);
    }

    private void haltTimer(long currentTime) {
        timer.cancel();
        view.setText(formatTime(currentTime));
    }

    private void resetTimer() {
        timer.cancel();
        view.setText(formatTime(0L));
    }


}


