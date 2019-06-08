package com.example.mike.mp3player.service.session;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.library.MediaLibrary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaSessionCallbackTest {
    /** object to test */
    private MediaSessionCallback mediaSessionCallback;
    @Mock
    private MediaPlaybackService mediaPlaybackService;
    @Mock
    private MediaSessionCompat mediaSession;
    @Mock
    private MediaLibrary mediaLibrary;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        when(mediaPlaybackService.getApplicationContext()).thenReturn(context);
        this.mediaSessionCallback = new MediaSessionCallback(mediaPlaybackService, mediaSession, mediaLibrary, Looper.myLooper());
    }

    @Test
    public void testPlay() {
        mediaSessionCallback.onPlay();
        assertTrue(true);
    }

}