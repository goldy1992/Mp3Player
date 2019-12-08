package com.github.goldy1992.mp3player.service.player

import android.os.Looper
import com.google.android.exoplayer2.PlaybackParameters
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class DecreaseSpeedProviderTest : SpeedProviderTestBase() {
    private var decreaseSpeedProvider: DecreaseSpeedProvider? = null
    @Before
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
        decreaseSpeedProvider = DecreaseSpeedProvider()
    }

    @Test
    fun testGetCustomAction() {
        val customAction = decreaseSpeedProvider!!.getCustomAction(exoPlayer!!)
        Assert.assertEquals(DECREASE_PLAYBACK_SPEED, customAction.action)
        Assert.assertEquals(DECREASE_PLAYBACK_SPEED, customAction.name)
    }

    @Test
    fun testDecreaseSpeed() {
        val currentSpeed = 1.0f
        val expectedSpeed = 0.95f
        Mockito.`when`(exoPlayer!!.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        decreaseSpeedProvider!!.onCustomAction(exoPlayer!!, controlDispatcher!!, DECREASE_PLAYBACK_SPEED, null)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(exoPlayer, Mockito.times(1)).setPlaybackParameters(captor!!.capture())
        val playbackParameters = captor!!.value
        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.0f)
    }

    /**
     * The speed SHOULD NOT decrease because it would under take the minimum
     */
    @Test
    fun testDecreaseSpeedInvalidSpeed() {
        val currentSpeed = 0.27f
        val expectedSpeed = 0.27f
        Mockito.`when`(exoPlayer!!.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        decreaseSpeedProvider!!.onCustomAction(exoPlayer!!, controlDispatcher!!, DECREASE_PLAYBACK_SPEED, null)
        Mockito.verify(exoPlayer, Mockito.never()).setPlaybackParameters(ArgumentMatchers.any<PlaybackParameters>())
        val playbackParameters = exoPlayer!!.playbackParameters
        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.00f)
    }
}