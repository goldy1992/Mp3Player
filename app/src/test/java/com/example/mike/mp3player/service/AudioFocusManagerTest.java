package com.example.mike.mp3player.service;


import android.content.Context;
import android.media.AudioManager;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AudioFocusManagerTest {

    private AudioFocusManager audioFocusManager;

    @Mock
    MediaPlayerAdapterBase mediaPlayerAdapterBase = mock(MediaPlayerAdapterBase.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        audioFocusManager = new AudioFocusManager(context, mediaPlayerAdapterBase);
        assertTrue(audioFocusManager.isInitialised());
    }

    /**
     *
     */
    @Test
    public void testRequestAudioFocus() {
        audioFocusManager.requestAudioFocus();
        assertTrue(audioFocusManager.hasFocus());
    }
    // TODO: Make a test that will request Audio Focus. Consider make this functionality
    //  asynchronous

    /**
     *
     */
    @Test
    public void testOnAudioFocusChangeAudioLossTransientCanDuck() {
        audioFocusManager.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK);
        assertTrue(audioFocusManager.hasFocus());
    }
    /**
     *
     */
    @Test
    public void testOnAudioFocusChangeAudioLossTransientAndGain() {
        when(mediaPlayerAdapterBase.isPlaying()).thenReturn(true);
        audioFocusManager.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT);
        assertFalse(audioFocusManager.hasFocus());
        when(mediaPlayerAdapterBase.isPlaying()).thenReturn(false);
        audioFocusManager.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
        assertTrue(audioFocusManager.hasFocus());
    }
    /**
     *
     */
    @Test
    public void testOnAudioFocusChangeAudioLoss() {
        audioFocusManager.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS);
        assertFalse(audioFocusManager.hasFocus());
    }


}