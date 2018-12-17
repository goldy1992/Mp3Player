package com.example.mike.mp3player.client;

import android.widget.TextView;

import com.example.mike.mp3player.client.view.TimeCounter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class TimeCounterTimerTaskTest {

    @Mock
    private TextView view;

    private TimeCounter timeCounter;
    private TimeCounterTimerTask timeCounterTimerTask;
    final long DURATION = 5000L;
    final long CURRENT_TIME = 4500L;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        timeCounter = new TimeCounter(view);
        timeCounterTimerTask = new TimeCounterTimerTask(timeCounter);
      //  when(timeCounter.getView().setText().then(doNothing());
    }

    @org.junit.jupiter.api.Test
    public void runTest() {
        timeCounter.setDuration(DURATION);
        timeCounter.setCurrentTime(CURRENT_TIME);
        timeCounterTimerTask.run();
        assertTrue("currentTime should be equal to the position parameter", timeCounter.getCurrentTime() > CURRENT_TIME);
    }
}