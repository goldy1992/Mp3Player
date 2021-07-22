package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager
import com.google.android.exoplayer2.ExoPlayer
import org.mockito.kotlin.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyControlDispatcherTest {
    private var myControlDispatcher: MyControlDispatcher? = null

    private val audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver = mock<AudioBecomingNoisyBroadcastReceiver>()

    private val playerNotificationManager: MyPlayerNotificationManager = mock<MyPlayerNotificationManager>()

    private val exoPlayer: ExoPlayer = mock<ExoPlayer>()

    @Before
    fun setup() {
        myControlDispatcher = MyControlDispatcher(audioBecomingNoisyBroadcastReceiver, playerNotificationManager)
    }

    @Test
    fun testDispatchSetPlayWhenReady() {
        whenever(playerNotificationManager.isActive).thenReturn(true)
        val result = myControlDispatcher!!.dispatchSetPlayWhenReady(exoPlayer, true)
        Assert.assertTrue(result)
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).register()
        verify(playerNotificationManager, never()).activate()
    }

    @Test
    fun testDispatchSetPlayWhenReadyPlaybackManagerNotActive() {
        whenever(playerNotificationManager.isActive).thenReturn(false)
        val result = myControlDispatcher!!.dispatchSetPlayWhenReady(exoPlayer, true)
        Assert.assertTrue(result)
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).register()
        verify(playerNotificationManager, times(1)).activate()
    }

    @Test
    fun testDispatchSetPlayWhenNotReady() {
        val result = myControlDispatcher!!.dispatchSetPlayWhenReady(exoPlayer, false)
        Assert.assertTrue(result)
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).unregister()
    }
}