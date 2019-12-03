package com.github.goldy1992.mp3player.client

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.utils.TimerUtils.calculateCurrentPlaybackPosition
import com.github.goldy1992.mp3player.client.utils.TimerUtils.convertToSeconds
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSystemClock

/**
 * Test class for the TimerUtils class
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [26], shadows = [ShadowSystemClock::class])
class TimerUtilsTest {
    @Test
    fun testConvertToSecondsOneSecond() {
        val milliseconds: Long = 1000
        val result = convertToSeconds(milliseconds)
        val expect = 1
        Assert.assertEquals(expect.toLong(), result.toLong())
    }

    @Test
    fun testConvertToSecondsMillisecondsToRound() {
        val milliseconds: Long = 17972
        val result = convertToSeconds(milliseconds)
        val expect = 17
        Assert.assertEquals(expect.toLong(), result.toLong())
    }

    @Test
    fun testFormatTimeOneSecond() {
        val milliseconds: Long = 1000
        val result = formatTime(milliseconds)
        val expect = "00:01"
        Assert.assertEquals(expect, result)
    }

    @Test
    fun testFormatTimeRoundableTime() {
        val milliseconds: Long = 78756
        val result = formatTime(milliseconds)
        val expect = "01:18"
        Assert.assertEquals(expect, result)
    }

    @Test
    fun calculateCurrentPlaybackPositionWhenStatePlayingTest() {
        val timeDiff = 400L
        val originalTime = SystemClock.elapsedRealtime() - timeDiff
        val originalPosition = 40000L
        val playbackStateCompat = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING,
                originalPosition, 0f, originalTime).build()
        val newPosition = calculateCurrentPlaybackPosition(playbackStateCompat)
        val expectedNewPosition = originalPosition + timeDiff
        Assert.assertEquals(expectedNewPosition, newPosition)
    }

    @Test
    fun calculateCurrentPlaybackPositionWhenNotStatePlayingTest() {
        val playbackStateCompat = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 40000L, 0f, 0).build()
        val newPosition = calculateCurrentPlaybackPosition(playbackStateCompat)
        Assert.assertEquals(playbackStateCompat.position, newPosition)
    }

    companion object {
        /**
         * Class setup method
         */
        @BeforeAll
        fun setupTests() {
            val currentTime = SystemClock.elapsedRealtime()
            val wrongCurrentTimeErrorMessage = "SystemClock.elapsedRealTime() (i.e. ShadowSystemClock)" +
                    " returned unexpected result. Tests are based on the assumption that this method will" +
                    " return" + SHADOW_SYSTEM_CLOCK_ELAPSED_TIME +
                    ". Consult the Robolectric documentation for more information"
            Assert.assertEquals(wrongCurrentTimeErrorMessage, SHADOW_SYSTEM_CLOCK_ELAPSED_TIME, currentTime)
        }

        /**
         * assumed elapsed time will return 100L.
         */
        private const val SHADOW_SYSTEM_CLOCK_ELAPSED_TIME = 100L
    }
}