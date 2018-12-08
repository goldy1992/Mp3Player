package com.example.mike.mp3player.client.view;

import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.TextView;

import com.example.mike.mp3player.client.PlaybackStateWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static android.support.v4.media.session.PlaybackStateCompat.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TimeCounterTest {

    @Mock
    private TextView view;

    private TimeCounter timeCounter;
    final long POSITION = 3424L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        timeCounter = new TimeCounter(view);
    }

    @Test
    public void updateStateResetTimerTest() {
        PlaybackStateWrapper state = createState(STATE_STOPPED, POSITION);
        timeCounter.setRunning(true);
        timeCounter.updateState(state);

        assertFalse("TimerCounter should not be running", timeCounter.isRunning());
        assertEquals("currentTime should be reset to 0", 0L, timeCounter.getCurrentTime());
    }

    @Test
    public void updateStateHaltTimerTest() {
        PlaybackStateWrapper state = createState(STATE_PAUSED, POSITION);
        timeCounter.updateState(state);

        assertFalse("TimerCounter should not be running", timeCounter.isRunning());
        assertEquals("currentTime should be equal to the position parameter", POSITION, timeCounter.getCurrentTime());
    }

    @Test
    public void updateStateWorkTest() {
        PlaybackStateWrapper state = createState(STATE_PLAYING, POSITION);
        timeCounter.updateState(state);

        assertTrue("TimerCounter should be running", timeCounter.isRunning());
    }

    private PlaybackStateWrapper createState(int state, long position) {
        PlaybackStateCompat playbackStateCompat = new Builder().setState(state, position, 0f, 0L).build();
       return new PlaybackStateWrapper(playbackStateCompat);
    }
}