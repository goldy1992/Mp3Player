package com.example.mike.mp3player.client;

import android.os.Handler;
import android.widget.TextView;

import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.views.TimeCounter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TimeCounterTimerTaskTest {

    @Mock
    private TextView view;
    @Mock
    private Handler mockHandler;

    private TimeCounter timeCounter;
    private TimeCounterTimerTask timeCounterTimerTask;
    final long DURATION = 5000L;
    final long CURRENT_TIME = 4500L;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        when(mockHandler.post(any())).then(doNothing());
        timeCounter = new TimeCounter(view);
        timeCounterTimerTask = new TimeCounterTimerTask(timeCounter, mockHandler);
      //  when(timeCounter.getView().setText().then(doNothing());
    }

    @Test
    public void runTest() {
        timeCounter.setDuration(DURATION);
        timeCounter.setCurrentPosition(CURRENT_TIME);
        timeCounterTimerTask.run();
        assertTrue("currentTime should be equal to the position parameter", timeCounter.getCurrentPosition() >= CURRENT_TIME);
    }
}