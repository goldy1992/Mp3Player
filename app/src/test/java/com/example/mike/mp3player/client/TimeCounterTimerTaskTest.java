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
    @Mock
    private MediaActivityCompat mediaActivityCompat;

    private TimeCounter timeCounter;
    private TimeCounterTimerTask timeCounterTimerTask;
    final long DURATION = 5000L;
    final long CURRENT_TIME = 4500L;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        timeCounter = new TimeCounter(mediaActivityCompat, view);
        timeCounterTimerTask = new TimeCounterTimerTask(timeCounter);
      //  when(timeCounter.getView().setText().then(doNothing());
    }

    @Test
    public void runTest() {
        timeCounter.setDuration(DURATION);
        timeCounter.setCurrentPosition(CURRENT_TIME);
        timeCounterTimerTask.run();
        assertTrue("currentTime should be equal to the position parameter", timeCounter.getCurrentPosition() > CURRENT_TIME);
    }
}