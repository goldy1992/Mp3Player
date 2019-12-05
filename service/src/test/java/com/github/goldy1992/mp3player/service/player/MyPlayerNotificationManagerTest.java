package com.github.goldy1992.mp3player.service.player;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.github.goldy1992.mp3player.service.MyDescriptionAdapter;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MyPlayerNotificationManagerTest {

    private MyPlayerNotificationManager myPlayerNotificationManager;

    private Context context;
    @Mock
    private MyDescriptionAdapter myDescriptionAdapter;

    @Mock
    private ExoPlayer exoPlayer;

    @Mock
    private PlayerNotificationManager playerNotificationManager;

    @Mock
    private PlayerNotificationManager.NotificationListener notificationListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.myPlayerNotificationManager = new MyPlayerNotificationManager(this.context, this.myDescriptionAdapter, this.exoPlayer, this.notificationListener);
        assertNotNull(myPlayerNotificationManager.getPlaybackNotificationManager());
        assertFalse(myPlayerNotificationManager.isActive());
    }

    @Test
    public void testActivate() {
        myPlayerNotificationManager.setPlayerNotificationManager(playerNotificationManager);
        myPlayerNotificationManager.activate();
        assertTrue(myPlayerNotificationManager.isActive());
        verify(playerNotificationManager, times(1)).setPlayer(exoPlayer);
    }

    @Test
    public void testDeactivate() {
        myPlayerNotificationManager.setPlayerNotificationManager(playerNotificationManager);
        myPlayerNotificationManager.deactivate();
        assertFalse(myPlayerNotificationManager.isActive());
        verify(playerNotificationManager, times(1)).setPlayer(null);
    }
}