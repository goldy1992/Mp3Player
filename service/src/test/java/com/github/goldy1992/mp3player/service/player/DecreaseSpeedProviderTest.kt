package com.github.goldy1992.mp3player.service.player

import android.os.Looper
import com.github.goldy1992.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED
import com.google.android.exoplayer2.PlaybackParameters
import org.mockito.kotlin.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class DecreaseSpeedProviderTest : SpeedProviderTestBase() {

    private lateinit var decreaseSpeedProvider: DecreaseSpeedProvider

    @Before
    fun setup() {
        decreaseSpeedProvider = DecreaseSpeedProvider()
    }

    @Test
    fun testGetCustomAction() {
        val customAction = decreaseSpeedProvider.getCustomAction(exoPlayer)
        Assert.assertEquals(DECREASE_PLAYBACK_SPEED, customAction.action)
        Assert.assertEquals(DECREASE_PLAYBACK_SPEED, customAction.name)
    }

    @Test
    fun testDecreaseSpeed() {
        val currentSpeed = 1.0f
        val expectedSpeed = 0.95f
        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        argumentCaptor<PlaybackParameters>().apply {
            decreaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, DECREASE_PLAYBACK_SPEED, null)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            verify(exoPlayer, times(1)).setPlaybackParameters(capture())
            val playbackParameters = firstValue
            Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.0f)
        }
    }

    /**
     * The speed SHOULD NOT decrease because it would under take the minimum
     */
    @Test
    fun testDecreaseSpeedInvalidSpeed() {
        val currentSpeed = 0.27f
        val expectedSpeed = 0.27f
        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        decreaseSpeedProvider.onCustomAction(exoPlayer, controlDispatcher, DECREASE_PLAYBACK_SPEED, null)
        verify(exoPlayer, never()).setPlaybackParameters(any<PlaybackParameters>())
        val playbackParameters = exoPlayer.playbackParameters
        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.00f)
    }
}