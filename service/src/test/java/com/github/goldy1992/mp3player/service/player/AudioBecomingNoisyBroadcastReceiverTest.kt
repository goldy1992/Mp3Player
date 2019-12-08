package com.github.goldy1992.mp3player.service.player

import android.content.Intent
import android.media.AudioManager
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.exoplayer2.ExoPlayer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AudioBecomingNoisyBroadcastReceiverTest {
    @Mock
    private val exoPlayer: ExoPlayer? = null
    @Spy
    private val context = InstrumentationRegistry.getInstrumentation().context
    /**
     * audio becoming noisy receiver
     */
    private var audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        audioBecomingNoisyBroadcastReceiver = AudioBecomingNoisyBroadcastReceiver(context, exoPlayer!!)
    }

    @Test
    fun testOnReceive() {
        val intent = Intent()
        intent.action = AudioManager.ACTION_AUDIO_BECOMING_NOISY
        audioBecomingNoisyBroadcastReceiver!!.onReceive(context, intent)
        /* Issue 64: we just want to update the playback state in this scenario, scpeifically to
         * state PAUSED */Mockito.verify(exoPlayer, Mockito.times(1))!!.playWhenReady = false
    }

    @Test
    fun testregister() { // confirm the audio noisy receiver is initially NOT set
        Assert.assertFalse(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        audioBecomingNoisyBroadcastReceiver!!.register()
        // assert that register receiver is called
        Mockito.verify(context, Mockito.times(1)).registerReceiver(ArgumentMatchers.any(), ArgumentMatchers.any())
        Assert.assertTrue(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        // reset invocation count
        Mockito.reset(context)
        audioBecomingNoisyBroadcastReceiver!!.register()
        // assert that register receiver is never called if there is already a receiver registered
        Mockito.verify(context, Mockito.never()).registerReceiver(ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun testUnregister() { // confirm the audio noisy receiver is initially NOT set
        Assert.assertFalse(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        audioBecomingNoisyBroadcastReceiver!!.unregister()
        // assert that unregister receiver is NEVER called
        Mockito.verify(context, Mockito.never()).unregisterReceiver(audioBecomingNoisyBroadcastReceiver)
        audioBecomingNoisyBroadcastReceiver!!.register()
        Assert.assertTrue(audioBecomingNoisyBroadcastReceiver!!.isRegistered)
        audioBecomingNoisyBroadcastReceiver!!.register()
        // assert that register receiver is never called if there is already a receiver registered
        audioBecomingNoisyBroadcastReceiver!!.unregister()
        Mockito.verify(context, Mockito.times(1)).registerReceiver(ArgumentMatchers.any(), ArgumentMatchers.any())
    }
}