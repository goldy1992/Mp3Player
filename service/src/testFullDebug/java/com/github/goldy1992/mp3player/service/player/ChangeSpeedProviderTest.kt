package com.github.goldy1992.mp3player.service.player

import android.os.Bundle
import android.os.Looper
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class ChangeSpeedProviderTest {

    private var exoPlayer: ExoPlayer = mock()

    private lateinit var changeSpeedProvider: ChangeSpeedProvider

    @Before
    fun setup() {
        changeSpeedProvider = ChangeSpeedProvider()
    }


    @Test
    fun testDecreaseSpeed() {
        val currentSpeed = 1.0f
        val expectedSpeed = 0.95f
        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
        argumentCaptor<PlaybackParameters>().apply {
            val bundle = Bundle()
            bundle.putFloat(CHANGE_PLAYBACK_SPEED, expectedSpeed)
            changeSpeedProvider.changeSpeed(exoPlayer, bundle)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            verify(exoPlayer, times(1)).setPlaybackParameters(capture())
            val playbackParameters = firstValue
            Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.0f)
        }
    }

//    /**
//     * The speed SHOULD NOT decrease because it would under take the minimum
//     */
//    @Test
//    fun testDecreaseSpeedInvalidSpeed() {
//        val currentSpeed = 0.27f
//        val expectedSpeed = 0.27f
//        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
//        changeSpeedProvider.changeSpeed(exoPlayer, null)
//        verify(exoPlayer, never()).setPlaybackParameters(any<PlaybackParameters>())
//        val playbackParameters = exoPlayer.playbackParameters
//        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.00f)
//    }
//
//    @Test
//    fun testIncreaseSpeed() {
//        val currentSpeed = 1.0f
//        val expectedSpeed = 1.05f
//        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
//        argumentCaptor<PlaybackParameters>().apply {
//            val bundle = Bundle()
//            bundle.putFloat(CHANGE_PLAYBACK_SPEED, expectedSpeed)
//            changeSpeedProvider.onCustomAction(exoPlayer, CHANGE_PLAYBACK_SPEED, bundle)
//            Shadows.shadowOf(Looper.getMainLooper()).idle()
//            verify(exoPlayer, times(1)).setPlaybackParameters(capture())
//            val playbackParameters = firstValue
//            Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.0f)
//        }
//    }
//
//    /**
//     * The speed SHOULD NOT increase because it would over take the maximum
//     */
//    @Test
//    fun testIncreaseSpeedInvalidSpeed() {
//        val currentSpeed = 1.98f
//        val expectedSpeed = 1.98f
//        whenever(exoPlayer.playbackParameters).thenReturn(PlaybackParameters(currentSpeed))
//        changeSpeedProvider.changeSpeed(exoPlayer, null)
//        verify(exoPlayer, never()).setPlaybackParameters(any<PlaybackParameters>())
//        val playbackParameters = exoPlayer.playbackParameters
//        Assert.assertEquals(expectedSpeed, playbackParameters.speed, 0.00f)
//    }
}