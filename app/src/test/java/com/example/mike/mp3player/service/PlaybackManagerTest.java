package com.example.mike.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PlaybackManagerTest {
    public PlaybackManager playbackManager;
    private static final QueueItem MOCK_QUEUE_ITEM = mock(QueueItem.class);


    @BeforeEach
    public void setup() {
        List<QueueItem> queueItems = new ArrayList<>();
        playbackManager = new PlaybackManager(queueItems);
    }
    /**
     * GIVEN: a Playback manager with an empty playlist
     * WHEN: we try to remove an item
     * THEN: no item is removed
     */
    @Test
    public void testOnRemoveQueueItemWithEmptyPlaylist() {
        final int EXPECTED = playbackManager.getQueueSize();

        playbackManager.onRemoveQueueItem(MOCK_QUEUE_ITEM);
        assertEquals(EXPECTED, playbackManager.getQueueSize());
    }
    /**
     * GIVEN: a Playback manager with an empty playlist
     * WHEN: we try to remove an item
     * THEN: no item is removed
     */
    @Test
    public void testOnAddAndOnRemoveQueueItem() {
        final int ORIGINAL_SIZE = playbackManager.getQueueSize();
        final int POST_ADD_SIZE = playbackManager.getQueueSize() + 1;
        playbackManager.onAddQueueItem(MOCK_QUEUE_ITEM);
        assertEquals(POST_ADD_SIZE, playbackManager.getQueueSize());
        playbackManager.onRemoveQueueItem(MOCK_QUEUE_ITEM);
        assertEquals(ORIGINAL_SIZE, playbackManager.getQueueSize());
    }
    /**
     * GIVEN: a playlist with 1 queue item
     * WHEN: we get the last index
     * THEN: zero should be returned
     */
    @Test
    public void testGetLastIndex() {
        final int EXPECTED_LAST_INDEX = 0;
        playbackManager.onAddQueueItem(MOCK_QUEUE_ITEM);
        assertEquals(EXPECTED_LAST_INDEX, playbackManager.getLastIndex());
    }
    /**
     * GIVEN: a empty playlist
     * WHEN: we get the last index
     * THEN: -1 should be returned
     */
    @Test
    public void testGetLastOnEmptyPlaylist() {
        final int EXPECTED_LAST_INDEX = -1;
        assertEquals(EXPECTED_LAST_INDEX, playbackManager.getLastIndex());
    }
}