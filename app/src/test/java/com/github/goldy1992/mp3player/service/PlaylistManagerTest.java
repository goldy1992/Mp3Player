package com.github.goldy1992.mp3player.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class PlaylistManagerTest {
    private PlaylistManager playlistManager;
    private static final MediaItem MOCK_QUEUE_ITEM = mock(MediaItem.class);


    @Before
    public void setup() {
        List<MediaItem> queueItems = new ArrayList<>();
        playlistManager = new PlaylistManager(queueItems);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateNewPlaylist() {
        List<MediaItem> originalPlaylist = Collections.emptyList();
        playlistManager = new PlaylistManager(originalPlaylist);
        assertEquals(originalPlaylist, playlistManager.getPlaylist());

        List<MediaItem> newPlaylist = java.util.Collections.emptyList();

        playlistManager.createNewPlaylist(newPlaylist);
        assertEquals(newPlaylist, playlistManager.getPlaylist());




    }

}