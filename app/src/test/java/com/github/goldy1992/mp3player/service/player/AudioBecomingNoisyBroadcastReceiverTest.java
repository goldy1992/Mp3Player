package com.github.goldy1992.mp3player.service.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.exoplayer2.ExoPlayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AudioBecomingNoisyBroadcastReceiverTest {
    @Mock
    private ExoPlayer exoPlayer;
    @Spy
    private Context context = InstrumentationRegistry.getInstrumentation().getContext();
    /**
     * audio becoming noisy receiver
     */
    private AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.audioBecomingNoisyBroadcastReceiver = new AudioBecomingNoisyBroadcastReceiver(context, exoPlayer);
    }

    @Test
    public void testOnReceive() {
        Intent intent = new Intent();
        intent.setAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        audioBecomingNoisyBroadcastReceiver.onReceive(context, intent);
        /* Issue 64: we just want to update the playback state in this scenario, scpeifically to
         * state PAUSED */
        verify(exoPlayer, times(1)).setPlayWhenReady(false);
    }

    @Test
    public void testregister() {
        // confirm the audio noisy receiver is initially NOT set
        assertFalse(audioBecomingNoisyBroadcastReceiver.isRegistered());
        audioBecomingNoisyBroadcastReceiver.register();
        // assert that register receiver is called
        verify(context, times(1)).registerReceiver(any(), any());
        assertTrue(audioBecomingNoisyBroadcastReceiver.isRegistered());
        // reset invocation count
        reset(context);

        audioBecomingNoisyBroadcastReceiver.register();
        // assert that register receiver is never called if there is already a receiver registered
        verify(context, never()).registerReceiver(any(), any());
    }

    @Test
    public void testUnregister() {
        // confirm the audio noisy receiver is initially NOT set
        assertFalse(audioBecomingNoisyBroadcastReceiver.isRegistered());
        audioBecomingNoisyBroadcastReceiver.unregister();
        // assert that unregister receiver is NEVER called
        verify(context, never()).unregisterReceiver(audioBecomingNoisyBroadcastReceiver);

        audioBecomingNoisyBroadcastReceiver.register();
        assertTrue(audioBecomingNoisyBroadcastReceiver.isRegistered());

        audioBecomingNoisyBroadcastReceiver.register();
        // assert that register receiver is never called if there is already a receiver registered
        audioBecomingNoisyBroadcastReceiver.unregister();
        verify(context, times(1)).registerReceiver(any(), any());

    }
}
