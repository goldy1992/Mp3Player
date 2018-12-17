package com.example.mike.mp3player.client;

import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.utils.TimerUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;


public class TimerUtilsTest {

    private static final long CURRENT_TIME = 50000L;

    @Test
    public void testConvertToSecondsOneSecond() {
        long milliseconds = 1000;
        int result = TimerUtils.convertToSeconds(milliseconds);
        int expect = 1;
        assertEquals(expect, result);
    }

    @Test
    public void testConvertToSecondsMilisecondsToRound() {
        long milliseconds = 17972;
        int result = TimerUtils.convertToSeconds(milliseconds);
        int expect = 17;
        assertEquals(expect, result);
    }

    @Test
    public void testFormatTimeOneSecond() {
        long milliseconds = 1000;
        String result = TimerUtils.formatTime(milliseconds);
        String expect = "00:01";
        assertEquals(expect, result);

    }
    @Test
    public void testFormatTimeRoundableTime() {
        long milliseconds = 78756;
        String result = TimerUtils.formatTime(milliseconds);
        String expect = "01:18";
        assertEquals(expect, result);
    }
    @Test
    public void calculateCurrentPlaybackPositionTest() {
     //   PowerMockito.mockStatic(System.class);
        final long timeDiff = 5000L;
     //   when(System.currentTimeMillis()).thenReturn(CURRENT_TIME);
        PlaybackStateCompat playbackStateCompat =
                new PlaybackStateCompat.Builder().setState(0, 40000L, 0f, 00).build();
        PlaybackStateWrapper playbackStateWrapper = new PlaybackStateWrapper(playbackStateCompat);
    //    when(System.currentTimeMillis()).thenReturn(CURRENT_TIME + timeDiff);
        long newPostion = TimerUtils.calculateCurrentPlaybackPosition(playbackStateWrapper);

        assertEquals(playbackStateCompat.getPosition() + timeDiff, newPostion);
    }


}