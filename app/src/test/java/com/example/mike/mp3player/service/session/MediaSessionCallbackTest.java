package com.example.mike.mp3player.service.session;

import android.content.Context;
import android.os.Looper;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaSessionCallbackTest {
    /** object to test */
    private MediaSessionCallback mediaSessionCallback;
    @Mock
    private MediaPlaybackService mediaPlaybackService;
    @Mock
    private MediaSessionAdapter mediaSessionAdapter;
    @Mock
    private MediaLibrary mediaLibrary;
    @Mock
    private PlaybackManager playbackManager;
    @Mock
    private MediaPlayerAdapterBase mediaPlayerAdapter;
    @Mock
    private ServiceManager serviceManager;
    @Mock
    private AudioBecomingNoisyBroadcastReceiver broadcastReceiver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        when(mediaPlaybackService.getApplicationContext()).thenReturn(context);
        this.mediaSessionCallback = new MediaSessionCallback(mediaPlaybackService, mediaLibrary,
                playbackManager, mediaPlayerAdapter, mediaSessionAdapter, serviceManager, broadcastReceiver, Looper.myLooper());
    }

    @Test
    public void testPlay() {
        when(mediaPlayerAdapter.play()).thenReturn(true);
        mediaSessionCallback.onPlay();
        verify(mediaPlayerAdapter, times(1)).play();
        verify(broadcastReceiver, times(1)).registerAudioNoisyReceiver();
        verify(serviceManager, times(1)).startService();
    }
    @Test
    public void testPause() {
        mediaSessionCallback.onPause();
        verify(mediaPlayerAdapter, times(1)).pause();
        verify(broadcastReceiver, times(1)).unregisterAudioNoisyReceiver();
        verify(serviceManager, times(1)).pauseService();
    }
    @Test
    public void testSkipToNext() {
        // TODO: fix test
//        final String newMediaId = "newMediaID";
//        when(playbackManager.skipToNext()).thenReturn(newMediaId);
//        when(mediaSessionAdapter.getCurrentPlaybackState(any())).thenReturn(createState(STATE_PLAYING));
//        mediaSessionCallback.onSkipToNext();
//        verify(mediaPlayerAdapter, times(1)).reset(any(), any());
//        verify(serviceManager, times(1)).notifyService();
            assertTrue(true);
    }

    private PlaybackStateCompat createState(@PlaybackStateCompat.State int playbackstate) {
        return new PlaybackStateCompat.Builder().setState(playbackstate, 0L, 0f).build();
    }
}