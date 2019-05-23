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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
@RunWith(RobolectricTestRunner.class)
public class AudioFocusManagerTest {

    private AudioFocusManager m_audioFocusManager;

    @Mock
    MediaPlayerAdapterBase mediaPlayerAdapterBase = mock(MediaPlayerAdapterBase.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        m_audioFocusManager = new AudioFocusManager(context, mediaPlayerAdapterBase);
        assertTrue(m_audioFocusManager.isInitialised());
    }

    // TODO: Make a test that will request Audio Focus. Consider make this functionality
    //  asynchronous
    @Test
    public void testOnAudioFocusChangeAudioFocusGain() {
        // TODO: fix test
        m_audioFocusManager.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
        //assertTrue(m_audioFocusManager.hasFocus());
    }


}