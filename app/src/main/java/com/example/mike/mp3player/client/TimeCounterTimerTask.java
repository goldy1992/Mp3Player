package com.example.mike.mp3player.client;

import android.util.Log;

import com.example.mike.mp3player.client.view.TimeCounter;

import java.util.TimerTask;

import static com.example.mike.mp3player.client.utils.TimerUtils.ONE_SECOND;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;

public class TimeCounterTimerTask extends TimerTask {
    private TimeCounter timeCounter;
    private String LOG_TAG = "TIMER_COUNTER_TASK";

    public TimeCounterTimerTask(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }

    @Override
    public void run() {
        //Log.d(LOG_TAG,"current position: " + timeCounter.getCurrentPosition() + ", duration: " + timeCounter.getDuration());
        if (timeCounter.getCurrentPosition() < timeCounter.getDuration()) {
            timeCounter.setCurrentPosition(timeCounter.getCurrentPosition() + ONE_SECOND);
            timeCounter.getParentActivity().runOnUiThread(updateUi);
        }
        //Log.d(LOG_TAG, "finished run call");
    }

    private Runnable updateUi = () -> timeCounter.getView().setText(formatTime(timeCounter.getCurrentPosition()));

}

