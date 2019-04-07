package com.example.mike.mp3player.client;

import android.os.Handler;

import com.example.mike.mp3player.client.views.TimeCounter;

import java.util.TimerTask;

import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;

public class TimeCounterTimerTask extends TimerTask {
    private TimeCounter timeCounter;
    private String LOG_TAG = "TIMER_COUNTER_TASK";
    private Runnable updateUi = () -> timeCounter.getView().setText(formatTime(timeCounter.getCurrentPosition()));
    private Handler mainHandler;

    public TimeCounterTimerTask(TimeCounter timeCounter, Handler mainHandler) {
        this.timeCounter = timeCounter;
        this.mainHandler = mainHandler;
    }

    @Override
    public void run() {
        //Log.d(LOG_TAG,"current position: " + timeCounter.getCurrentPosition() + ", duration: " + timeCounter.getDuration());
        long newPosition = timeCounter.getCurrentPosition() + ONE_SECOND;
        if (newPosition < timeCounter.getDuration()) {
            timeCounter.setCurrentPosition(newPosition);
            mainHandler.post(updateUi);
        } else if (timeCounter.isRepeating()) {
            timeCounter.setCurrentPosition(DEFAULT_POSITION);
            mainHandler.post(updateUi);
        }

        //Log.d(LOG_TAG, "finished run call");
    } // run
}