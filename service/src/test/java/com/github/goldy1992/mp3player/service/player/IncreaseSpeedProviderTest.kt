package com.github.goldy1992.mp3player.service.player

import android.os.Looper
import com.github.goldy1992.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED
import com.google.android.exoplayer2.PlaybackParameters
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.capture
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class IncreaseSpeedProviderTest : SpeedProviderTestBase() {
    private lateinit var increaseSpeedProvider: IncreaseSpeedProvider
    @Before
    override fun setup() {

        super.setup()
        increaseSpeedProvider = IncreaseSpeedProvider()
    }

    @Test
    fun testGetCustomAction() {
        val customAction = increaseSpeedProvider!!.getCustomAction(exoPlayer!!)
        Assert.assertEquals(INCREASE_PLAYBACK_SPEED, customAction.action)
        Assert.assertEquals(INCREASE_PLAYBACK_SPEED, customAction.name)
    }

    @Test
    fun testIncreaseSpeed() {
        val currentSpeed = 1.0f
        val expectedSpeed = 1.05f
        whenever(exoPlayer!!.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        argumentCaptor<PlaybackParameters>().apply {
            increaseSpeedProvider!!.onCustomAction(exoPlayer, controlDispatcher!!, INCREASE_PLAYBACK_SPEED, null)
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
        Mockito.`when`(exoPlayer!!.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        increaseSpeedProvider!!.onCustomAction(exoPlayer!!, controlDispatcher!!, INCREASE_PLAYBACK_SPEED, null)
        Mockito.verify(exoPlayer, Mockito.never()).setPlaybackParameters(ArgumentMatchers.any<PlaybackParameters>())
        val playbackParameters = exoPlayer!!.playbackParameters
        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.00f)
    }
}