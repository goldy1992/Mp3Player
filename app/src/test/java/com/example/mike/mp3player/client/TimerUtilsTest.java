package com.example.mike.mp3player.client;

import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.service.ShadowBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.ShadowSystemClock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * In order to use Powermockito, used old Test tag so that the class runs with JUnit4.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 26, shadows = {ShadowSystemClock.class})
@PrepareForTest({SystemClock.class})
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
    public void calculateCurrentPlaybackPositionWhenStatePlayingTest() {
        final long timeDiff = 5000L;
        PlaybackStateCompat playbackStateCompat =
                new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 40000L, 0f, CURRENT_TIME).build();
        long newPostion = TimerUtils.calculateCurrentPlaybackPosition(playbackStateCompat, 35000);

        assertEquals(playbackStateCompat.getPosition() + timeDiff, newPostion);
    }

    @Test
    public void calculateCurrentPlaybackPositionWhenNotStatePlayingTest() {
        final long timeDiff = 5000L;
        PlaybackStateCompat playbackStateCompat =
                new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 40000L, 0f, CURRENT_TIME).build();
        long newPostion = TimerUtils.calculateCurrentPlaybackPosition(playbackStateCompat, 45000L);

        assertEquals(playbackStateCompat.getPosition(), newPostion);
    }


}