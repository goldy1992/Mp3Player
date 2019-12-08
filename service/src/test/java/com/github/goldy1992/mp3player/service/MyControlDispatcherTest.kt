package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager
import com.google.android.exoplayer2.ExoPlayer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyControlDispatcherTest {
    private var myControlDispatcher: MyControlDispatcher? = null
    @Mock
    private val audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver? = null
    @Mock
    private val playerNotificationManager: MyPlayerNotificationManager? = null
    @Mock
    private val exoPlayer: ExoPlayer? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        myControlDispatcher = MyControlDispatcher(audioBecomingNoisyBroadcastReceiver!!, playerNotificationManager!!)
    }

    @Test
    fun testDispatchSetPlayWhenReady() {
        Mockito.`when`(playerNotificationManager!!.isActive).thenReturn(true)
        val result = myControlDispatcher!!.dispatchSetPlayWhenReady(exoPlayer!!, true)
        Assert.assertTrue(result)
        Mockito.verify(audioBecomingNoisyBroadcastReceiver, Mockito.times(1))!!.register()
        Mockito.verify(playerNotificationManager, Mockito.never())!!.activate()
    }

    @Test
    fun testDispatchSetPlayWhenReadyPlaybackManagerNotActive() {
        Mockito.`when`(playerNotificationManager!!.isActive).thenReturn(false)
        val result = myControlDispatcher!!.dispatchSetPlayWhenReady(exoPlayer!!, true)
        Assert.assertTrue(result)
        Mockito.verify(audioBecomingNoisyBroadcastReceiver, Mockito.times(1))!!.register()
        Mockito.verify(playerNotificationManager, Mockito.times(1))!!.activate()
    }

    @Test
    fun testDispatchSetPlayWhenNotReady() {
        val result = myControlDispatcher!!.dispatchSetPlayWhenReady(exoPlayer!!, false)
        Assert.assertTrue(result)
        Mockito.verify(audioBecomingNoisyBroadcastReceiver, Mockito.times(1))!!.unregister()
    }
}