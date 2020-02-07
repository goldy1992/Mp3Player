package com.github.goldy1992.mp3player.service.player

import android.content.Intent
import android.media.AudioManager
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.exoplayer2.ExoPlayer
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AudioBecomingNoisyBroadcastReceiverTest {

    private val exoPlayer: ExoPlayer = mock<ExoPlayer>()

    private val context = spy(InstrumentationRegistry.getInstrumentation().context)
    /**
     * audio becoming noisy receiver
     */
    private var audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver? = null

    @Before
    fun setup() {
        audioBecomingNoisyBroadcastReceiver = AudioBecomingNoisyBroadcastReceiver(context, exoPlayer)
    }

    @Test
    fun testOnReceive() {
        val intent = Intent()
        intent.action = AudioManager.ACTION_AUDIO_BECOMING_NOISY
        audioBecomingNoisyBroadcastReceiver!!.onReceive(context, intent)
        /* Issue 64: we just want to update the playback state in this scenario, scpeifically to
         * state PAUSED */verify(exoPlayer, times(1)).playWhenReady = false
    }

    @Test
    fun testregister() { // confirm the audio noisy receiver is initially NOT set
        Assert.assertFalse(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        audioBecomingNoisyBroadcastReceiver!!.register()
        // assert that register receiver is called
        verify(context, times(1)).registerReceiver(any(), any())
        Assert.assertTrue(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        // reset invocation count
        reset(context)
        audioBecomingNoisyBroadcastReceiver!!.register()
        // assert that register receiver is never called if there is already a receiver registered
        verify(context, never()).registerReceiver(any(), any())
    }

    @Test
    fun testUnregister() { // confirm the audio noisy receiver is initially NOT set
        Assert.assertFalse(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        audioBecomingNoisyBroadcastReceiver!!.unregister()
        // assert that unregister receiver is NEVER called
        verify(context, never()).unregisterReceiver(audioBecomingNoisyBroadcastReceiver)
        audioBecomingNoisyBroadcastReceiver!!.register()
        Assert.assertTrue(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        audioBecomingNoisyBroadcastReceiver!!.register()
        // assert that register receiver is never called if there is already a receiver registered
        audioBecomingNoisyBroadcastReceiver!!.unregister()
        verify(context, times(1)).registerReceiver(any(), any())
    }
}