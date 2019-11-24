package com.github.goldy1992.mp3player.service;

import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver;
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager;
import com.google.android.exoplayer2.ExoPlayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MyControlDispatcherTest {

    private MyControlDispatcher myControlDispatcher;

    @Mock
    private AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver;

    @Mock
    private MyPlayerNotificationManager playerNotificationManager;

    @Mock
    private ExoPlayer exoPlayer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.myControlDispatcher = new MyControlDispatcher(audioBecomingNoisyBroadcastReceiver, playerNotificationManager);
    }

    @Test
    public void testDispatchSetPlayWhenReady() {
        when(playerNotificationManager.isActive()).thenReturn(true);
        final boolean result = myControlDispatcher.dispatchSetPlayWhenReady(exoPlayer, true);
        assertTrue(result);
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).register();
        verify(playerNotificationManager, never()).activate();
    }

    @Test
    public void testDispatchSetPlayWhenReadyPlaybackManagerNotActive() {
        when(playerNotificationManager.isActive()).thenReturn(false);
        final boolean result = myControlDispatcher.dispatchSetPlayWhenReady(exoPlayer, true);
        assertTrue(result);
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).register();
        verify(playerNotificationManager, times(1)).activate();
    }

    @Test
    public void testDispatchSetPlayWhenNotReady() {
        final boolean result = myControlDispatcher.dispatchSetPlayWhenReady(exoPlayer, false);
        assertTrue(result);
        verify(audioBecomingNoisyBroadcastReceiver, times(1)).unregister();
    }

}