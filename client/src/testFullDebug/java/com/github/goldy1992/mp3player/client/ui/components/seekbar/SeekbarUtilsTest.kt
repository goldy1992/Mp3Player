package com.github.goldy1992.mp3player.client.ui.components.seekbar

import com.github.goldy1992.mp3player.client.utils.SeekbarUtils
import org.junit.Assert.*
import org.junit.Test

class SeekbarUtilsTest {

    @Test
    fun testCalculateAnimationTime() {
        val duration = 100000f
        val currentPosition = 50000f
        val playbackSpeed = 1.0f
        val expectedResult = 50000
        val result = SeekbarUtils.calculateAnimationTime(
            currentPosition = currentPosition,
            duration = duration,
            playbackSpeed = playbackSpeed
        )
        assertEquals(expectedResult, result)
    }

    /**
     * GIVEN:
     * duration: 10000, currentPosition 50000, speed: 1.5 (milliseconds (ms))
     *
     * remaining time at playback speed 1.0 would be (100,000 - 50,000) = 50,000
     * => expecting a value < 50,000
     *
     * 50,000 / 1.5 = 33,333 ms
     */
    @Test
    fun testCalculateAnimationTimeForFasterPlaybackSpeed() {
        val duration = 100000f
        val currentPosition = 50000f
        val playbackSpeed = 1.5f
        val expectedResult = 33333
        val result = SeekbarUtils.calculateAnimationTime(
            currentPosition = currentPosition,
            duration = duration,
            playbackSpeed = playbackSpeed
        )
        assertEquals(expectedResult, result)
    }

    /**
     * GIVEN:
     * duration: 10000, currentPosition 50000, speed: 1.5 (milliseconds (ms))
     *
     * remaining time at playback speed 1.0 would be (100,000 - 50,000) = 50,000
     * => expecting a value > 50,000
     *
     * 50,000 / 0.5 = 100,000 ms
     */
    @Test
    fun testCalculateAnimationTimeForSlowerPlaybackSpeed() {
        val duration = 100000f
        val currentPosition = 50000f
        val playbackSpeed = 0.5f
        val expectedResult = 100000
        val result = SeekbarUtils.calculateAnimationTime(
            currentPosition = currentPosition,
            duration = duration,
            playbackSpeed = playbackSpeed
        )
        assertEquals(expectedResult, result)
    }
}