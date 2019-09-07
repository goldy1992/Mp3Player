package com.example.mike.mp3player.client.views;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.ScheduledExecutorService;

import static android.support.v4.media.session.PlaybackStateCompat.Builder;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_STOPPED;
import static com.example.mike.mp3player.client.utils.TimerUtils.formatTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class TimeCounterTest {
    @Mock
    private TextView view;
    private  Handler handler;
    private TimeCounter timeCounter;
    final long POSITION = 3424L;
    final long DURATION = 100000L;

    @Before
    public void setup() {
        this.handler = new Handler(Looper.getMainLooper());
        MockitoAnnotations.initMocks(this);
        timeCounter = new TimeCounter(handler);
        timeCounter.init(view);
    }
    @Test
    public void testNotInitialised() {
        final long expectedPosition = 0L;
        final int expectedState = STATE_PLAYING;
        timeCounter.init(null);
        timeCounter.updateState(createState(expectedState, 5L));
        assertEquals(expectedState, timeCounter.getCurrentState());

        // current position will not be updated will not updated as text view is null
        assertEquals(expectedPosition, timeCounter.getCurrentPosition());

    }

    @Test
    public void updateStateResetTimerTest() {
        PlaybackStateCompat state = createState(STATE_STOPPED, POSITION);
        timeCounter.updateState(state);
        assertFalse("TimerCounter should not be running", timeCounter.isRunning());
        assertEquals("currentTime should be reset to 0", 0L, timeCounter.getCurrentPosition());
    }

    @Test
    public void updateStateHaltTimerTest() {
        setStatePlaying();
        ScheduledExecutorService spiedTimer = spy(timeCounter.getTimer());
        timeCounter.setTimer(spiedTimer);
        PlaybackStateCompat state = createState(STATE_PAUSED, POSITION);
        timeCounter.updateState(state);
        verify(spiedTimer).shutdown();
        assertFalse("TimerCounter should not be running", timeCounter.isRunning());
        assertEquals("currentTime should be equal to the position parameter", POSITION, timeCounter.getCurrentPosition());
    }

    @Test
    public void updateStateWorkTest() {
        timeCounter.setDuration(DURATION);
        PlaybackStateCompat state = createState(STATE_PLAYING, POSITION);
        timeCounter.updateState(state);
        assertTrue("TimerCounter should be running", timeCounter.isRunning());
    }

    @Test
    public void testSeekTo() {
        final long position = 28L;
        final String expectedText = formatTime(position);
        timeCounter.seekTo(position);
        verify(view, times(1)).setText(expectedText);
        assertEquals(position, timeCounter.getCurrentPosition());
    }

    private PlaybackStateCompat createState(int state, long position) {
        return new Builder().setState(state, position, 0f, 0L).build();
    }

    private void setStatePlaying() {
        PlaybackStateCompat state = createState(STATE_PLAYING, POSITION);
        timeCounter.updateState(state);
    }
}