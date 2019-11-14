package com.github.goldy1992.mp3player.service;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static org.mockito.Mockito.mock;

public class PlaylistManagerTest {
    private PlaylistManager playlistManager;
    private static final MediaItem MOCK_QUEUE_ITEM = mock(MediaItem.class);


    @BeforeEach
    public void setup() {
        List<MediaItem> queueItems = new ArrayList<>();
        playlistManager = new PlaylistManager(queueItems, -1);
    }

}