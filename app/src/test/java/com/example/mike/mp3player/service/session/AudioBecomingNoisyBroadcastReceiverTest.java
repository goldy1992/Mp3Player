package com.example.mike.mp3player.service.session;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AudioBecomingNoisyBroadcastReceiverTest {
    @Mock
    private MediaPlayerAdapterBase mediaPlayerAdapterBase;
    @Mock
    private MediaSessionAdapter mediaSessionAdapter;
    @Mock
    private ServiceManager serviceManager;
    @Spy
    private Context context = InstrumentationRegistry.getInstrumentation().getContext();
    /** audio becoming noisy receiver */
    private AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.audioBecomingNoisyBroadcastReceiver = new AudioBecomingNoisyBroadcastReceiver(context,
                mediaSessionAdapter, mediaPlayerAdapterBase, serviceManager);
    }

    @Test
    public void testOnReceive() {
        // TODO: complete test
        assertTrue(true);
    }

    @Test
    public void testRegisterAudioNoisyReceiver() {
        // confirm the audio noisy receiver is initially NOT set
        assertFalse(audioBecomingNoisyBroadcastReceiver.isAudioNoisyReceiverRegistered());
        audioBecomingNoisyBroadcastReceiver.registerAudioNoisyReceiver();
        // assert that register receiver is called
        verify(context, times(1)).registerReceiver(any(), any());
        assertTrue(audioBecomingNoisyBroadcastReceiver.isAudioNoisyReceiverRegistered());
        // reset invocation count
        reset(context);

        audioBecomingNoisyBroadcastReceiver.registerAudioNoisyReceiver();
        // assert that register receiver is never called if there is already a receiver registered
        verify(context, never()).registerReceiver(any(), any());
    }

    @Test
    public void testUnregisterAudioNoisyReceiver() {
        // confirm the audio noisy receiver is initially NOT set
        assertFalse(audioBecomingNoisyBroadcastReceiver.isAudioNoisyReceiverRegistered());
        audioBecomingNoisyBroadcastReceiver.unregisterAudioNoisyReceiver();
        // assert that unregister receiver is NEVER called
        verify(context, never()).unregisterReceiver(audioBecomingNoisyBroadcastReceiver);

        audioBecomingNoisyBroadcastReceiver.registerAudioNoisyReceiver();
        assertTrue(audioBecomingNoisyBroadcastReceiver.isAudioNoisyReceiverRegistered());

        audioBecomingNoisyBroadcastReceiver.registerAudioNoisyReceiver();
        // assert that register receiver is never called if there is already a receiver registered
        audioBecomingNoisyBroadcastReceiver.unregisterAudioNoisyReceiver();
        verify(context, times(1)).registerReceiver(any(), any());

    }
}