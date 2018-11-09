package com.example.mike.mp3player.client;

import com.example.mike.mp3player.client.view.TimeCounter;

import java.util.TimerTask;

import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;

public class TimeCounterTimerTask extends TimerTask {
    private TimeCounter timeCounter;

    public TimeCounterTimerTask(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }

    @Override
    public void run() {
        if (timeCounter.getCurrentTime() < timeCounter.getDuration()) {
            timeCounter.setCurrentTime(timeCounter.getCurrentTime() + ONE_SECOND);
            timeCounter.getView().setText(formatTime(timeCounter.getCurrentTime()));
        }
    }
}

