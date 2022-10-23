package com.github.goldy1992.mp3player.service

import androidx.media3.exoplayer.ExoPlayer
import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyForwardingPlayerTest {
    private var forwardingPlayer: MyForwardingPlayer? = null

    private val audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver = mock<AudioBecomingNoisyBroadcastReceiver>()

    private val exoPlayer: ExoPlayer = mock<ExoPlayer>()

    @Before
    fun setup() {
        forwardingPlayer = MyForwardingPlayer(exoPlayer, audioBecomingNoisyBroadcastReceiver)
    }

    @Test
    fun testDispatchSetPlayWhenReady() {
        forwardingPlayer!!.setPlayWhenReady(true)
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).register()

    }

    @Test
    fun testDispatchSetPlayWhenReadyPlaybackManagerNotActive() {
        forwardingPlayer!!.setPlayWhenReady(true)
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).register()
    }

    @Test
    fun testDispatchSetPlayWhenNotReady() {
        forwardingPlayer!!.setPlayWhenReady(false)
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).unregister()
    }
}