package com.example.mike.mp3player.client;

import android.os.Handler;

import com.example.mike.mp3player.client.views.TimeCounter;

import java.util.TimerTask;

import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;

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
        if (timeCounter.getCurrentPosition() < timeCounter.getDuration()) {
            timeCounter.setCurrentPosition(timeCounter.getCurrentPosition() + ONE_SECOND);
            mainHandler.post(updateUi);
        }
        //Log.d(LOG_TAG, "finished run call");
    } // run
}

