package com.example.mike.mp3player.client;

import android.os.Handler;
import android.os.Looper;

import com.example.mike.mp3player.client.view.TimeCounter;

import java.util.TimerTask;

import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;

public class TimeCounterTimerTask extends TimerTask {
    private TimeCounter timeCounter;
    private String LOG_TAG = "TIMER_COUNTER_TASK";
    private Runnable updateUi = () -> timeCounter.getView().setText(formatTime(timeCounter.getCurrentPosition()));

    public TimeCounterTimerTask(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }

    @Override
    public void run() {
        //Log.d(LOG_TAG,"current position: " + timeCounter.getCurrentPosition() + ", duration: " + timeCounter.getDuration());
        if (timeCounter.getCurrentPosition() < timeCounter.getDuration()) {
            timeCounter.setCurrentPosition(timeCounter.getCurrentPosition() + ONE_SECOND);
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(updateUi);
        }
        //Log.d(LOG_TAG, "finished run call");
    } // run
}

