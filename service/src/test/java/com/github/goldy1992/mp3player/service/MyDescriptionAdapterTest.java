package com.github.goldy1992.mp3player.service;

import android.content.Context;
import android.media.session.MediaSession;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.github.goldy1992.mp3player.commons.ComponentClassMapper;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.google.android.exoplayer2.ExoPlayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MyDescriptionAdapterTest {

    @Mock
    private PlaylistManager playlistManager;
    @Mock
    private ExoPlayer player;

    private MyDescriptionAdapter myDescriptionAdapter;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        MediaSessionCompat.Token token = getMediaSessionCompatToken(context);
        ComponentClassMapper componentClassMapper = new ComponentClassMapper.Builder().build();
        this.myDescriptionAdapter = new MyDescriptionAdapter(context, token, playlistManager, componentClassMapper);
    }

    @Test
    public void testGetCurrentContentTitle() {
        final String expectedTitle = "title";
        final MediaItem testItem = new MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build();
        final int index = 7;
        when(player.getCurrentWindowIndex()).thenReturn(index);
        when(playlistManager.getItemAtIndex(index)).thenReturn(testItem);

        final String result = myDescriptionAdapter.getCurrentContentTitle(player);
        assertEquals(expectedTitle, result);
    }

    private MediaSessionCompat.Token getMediaSessionCompatToken(Context context) {
        MediaSession mediaSession = new MediaSession(context, "sd");
        MediaSession.Token sessionToken = mediaSession.getSessionToken();
        return MediaSessionCompat.Token.fromToken(sessionToken);
    }

}