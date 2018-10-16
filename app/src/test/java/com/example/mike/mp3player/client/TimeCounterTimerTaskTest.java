package com.example.mike.mp3player.client;

import android.widget.TextView;

import com.example.mike.mp3player.client.view.TimeCounter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TimeCounterTimerTaskTest {

    @Mock
    private TextView view;

    private TimeCounter timeCounter;
    private TimeCounterTimerTask timeCounterTimerTask;
    final long DURATION = 5000L;
    final long CURRENT_TIME = 4500L;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        timeCounter = new TimeCounter(view);
        timeCounterTimerTask = new TimeCounterTimerTask(timeCounter);
      //  when(timeCounter.getView().setText().then(doNothing());
    }

    @Test
    public void runTest() {
        timeCounter.setDuration(DURATION);
        timeCounter.setCurrentTime(CURRENT_TIME);
        timeCounterTimerTask.run();
        assertTrue("currentTime should be equal to the position parameter", timeCounter.getCurrentTime() > CURRENT_TIME);
    }
}