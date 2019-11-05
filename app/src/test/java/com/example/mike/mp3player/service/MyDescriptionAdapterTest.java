package com.example.mike.mp3player.service;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.commons.MediaItemBuilder;
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
    private PlaybackManager playbackManager;
    @Mock
    private ExoPlayer player;

    private MyDescriptionAdapter myDescriptionAdapter;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        MediaSessionCompat.Token token = TestUtils.getMediaSessionCompatToken(context);
        this.myDescriptionAdapter = new MyDescriptionAdapter(context, token, playbackManager);
    }

    @Test
    public void testGetCurrentContentTitle() {
        final String expectedTitle = "title";
        final MediaItem testItem = new MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build();
        final int index = 7;
        when(player.getCurrentWindowIndex()).thenReturn(index);
        when(playbackManager.getItemAtIndex(index)).thenReturn(testItem);

        final String result = myDescriptionAdapter.getCurrentContentTitle(player);
        assertEquals(expectedTitle, result);
    }

}