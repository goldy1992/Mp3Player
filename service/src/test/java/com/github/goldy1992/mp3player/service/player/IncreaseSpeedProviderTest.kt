package com.github.goldy1992.mp3player.service.player

import android.os.Looper
import com.github.goldy1992.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED
import com.google.android.exoplayer2.PlaybackParameters
import org.mockito.kotlin.*

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class IncreaseSpeedProviderTest : SpeedProviderTestBase() {
    private lateinit var increaseSpeedProvider: IncreaseSpeedProvider

    @Before
    fun setup() {
        increaseSpeedProvider = IncreaseSpeedProvider()
    }

    @Test
    fun testGetCustomAction() {
        val customAction = increaseSpeedProvider.getCustomAction(exoPlayer)
        Assert.assertEquals(INCREASE_PLAYBACK_SPEED, customAction.action)
        Assert.assertEquals(INCREASE_PLAYBACK_SPEED, customAction.name)
    }

    @Test
    fun testIncreaseSpeed() {
        val currentSpeed = 1.0f
        val expectedSpeed = 1.05f
        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        argumentCaptor<PlaybackParameters>().apply {
            increaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, INCREASE_PLAYBACK_SPEED, null)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            verify(exoPlayer, times(1)).setPlaybackParameters(capture())
            val playbackParameters = firstValue
            Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.0f)
        }
    }

    /**
     * The speed SHOULD NOT increase because it would over take the maximum
     */
    @Test
    fun testIncreaseSpeedInvalidSpeed() {
        val currentSpeed = 1.98f
        val expectedSpeed = 1.98f
        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        increaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, INCREASE_PLAYBACK_SPEED, null)
        verify(exoPlayer, never()).setPlaybackParameters(any<PlaybackParameters>())
        val playbackParameters = exoPlayer.playbackParameters
        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.00f)
    }
}