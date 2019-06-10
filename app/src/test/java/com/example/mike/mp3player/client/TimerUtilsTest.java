package com.example.mike.mp3player.client;

import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.utils.TimerUtils;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSystemClock;

import static org.junit.Assert.assertEquals;


/**
 * In order to use Powermockito, used old Test tag so that the class runs with JUnit4.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowSystemClock.class})
public class TimerUtilsTest {
    /**
     * Class setup method
     */
    @BeforeAll
    public static void setupTests() {
        final long currentTime = SystemClock.elapsedRealtime();
        String wrongCurrentTimeErrorMessage = "SystemClock.elapsedRealTime() (i.e. ShadowSystemClock)" +
                " returned unexpected result. Tests are based on the assumption that this method will" +
                " return" + SHADOW_SYSTEM_CLOCK_ELAPSED_TIME +
                ". Consult the Robolectric documentation for more information";
        assertEquals(wrongCurrentTimeErrorMessage, SHADOW_SYSTEM_CLOCK_ELAPSED_TIME, currentTime);
    }
    /**
     * assumed elapsed time will return 100L.
     */
    private static final long SHADOW_SYSTEM_CLOCK_ELAPSED_TIME = 100L;

    @Test
    public void testConvertToSecondsOneSecond() {
        long milliseconds = 1000;
        int result = TimerUtils.convertToSeconds(milliseconds);
        int expect = 1;
        assertEquals(expect, result);
    }

    @Test
    public void testConvertToSecondsMillisecondsToRound() {
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
    public void calculateCurrentPlaybackPositionWhenStatePlayingTest() {
        final long timeDiff = 400L;
        final long originalTime = SystemClock.elapsedRealtime() - timeDiff;
        final long originalPosition = 40000L;
        PlaybackStateCompat playbackStateCompat =
                new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING,
                        originalPosition, 0f, originalTime).build();
        final long newPosition = TimerUtils.calculateCurrentPlaybackPosition(playbackStateCompat);
        final long expectedNewPosition = originalPosition + timeDiff;
        assertEquals(expectedNewPosition, newPosition);
    }

    @Test
    public void calculateCurrentPlaybackPositionWhenNotStatePlayingTest() {
        PlaybackStateCompat playbackStateCompat =
                new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 40000L, 0f, 0).build();
        long newPosition = TimerUtils.calculateCurrentPlaybackPosition(playbackStateCompat);
        assertEquals(playbackStateCompat.getPosition(), newPosition);
    }


}