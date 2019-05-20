package com.example.mike.mp3player.service;

import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    /**
     * GIVEN: a playlist with 1 queue item at index 0
     * WHEN: we get the media id of index 0
     */
    @Test
    public void getPlaylistMediaId() {
        QueueItem queueItem = MOCK_QUEUE_ITEM;
        final int QUEUE_ITEM_INDEX = 0;
        final String EXPECTED_MEDIA_ID = "EXPECTED_MEDIA_ID";
        MediaDescriptionCompat description = mock(MediaDescriptionCompat.class);
        when(description.getMediaId()).thenReturn(EXPECTED_MEDIA_ID);
        when(queueItem.getDescription()).thenReturn(description);
        playbackManager.onAddQueueItem(queueItem);
        String result = playbackManager.getPlaylistMediaId(QUEUE_ITEM_INDEX);
        assertEquals(EXPECTED_MEDIA_ID, result);
    }
    /**
     * GIVEN: a playlist of 2 items and the playlist is repeating
     * AND: the current item is the first item
     * WHEN: notifyPlaybackComplete() is called
     * THEN: the new queue index is the 2nd queue item
     */
    @Test
    public void testNotifyPlaybackCompleteNotLastItem() {
        final QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        final QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.setRepeating(true);
        QueueItem currentItem = playbackManager.getCurrentItem();
        assertEquals(ITEM1, currentItem);
        playbackManager.notifyPlaybackComplete();
        currentItem = playbackManager.getCurrentItem();
        assertEquals(ITEM2, currentItem);
        playbackManager.notifyPlaybackComplete();
        currentItem = playbackManager.getCurrentItem();
        assertEquals(ITEM1, currentItem);
    }
    /**
     * GIVEN: a playlist of 2 items and the playlist is repeating
     * AND: the current item is the last item in the list
     * WHEN: notifyPlaybackComplete() is called subsequently
     * THEN: the new queue index returns to be the first item since repeating is set to true.
     */
    @Test
    public void testNotifyPlaybackCompleteLastItemRepeating() {
        final QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        final QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.setRepeating(true);
        playbackManager.notifyPlaybackComplete(); //increment to the last item
        playbackManager.notifyPlaybackComplete(); // incrementation on the last item
        QueueItem currentItem = playbackManager.getCurrentItem();
        assertEquals(ITEM1, currentItem);
    }
    /**
     * GIVEN: a playlist of 2 items and the playlist is NOT repeating
     * AND: the current item is the last item in the list
     * WHEN: notifyPlaybackComplete() is called subsequently
     * THEN: the new queue index should return null because it was already at the last item in the
     * list.
     */
    @Test
    public void testNotifyPlaybackCompleteLastItemNotRepeating() {
        final QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        final QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.setRepeating(false);
        playbackManager.notifyPlaybackComplete(); //increment to the last item
        playbackManager.notifyPlaybackComplete(); // incrementation on the last item
        QueueItem currentItem = playbackManager.getCurrentItem();
        assertNull(currentItem);
    }
    /**
     * GIVEN: a playlist of 3 items
     * WHEN: setCurrentItem() is called with the media id of the last item
     * THEN: the last item is the new current item
     */
    @Test
    public void testGetCurrentItem() {
        final String MEDIA_ID = "MEDIA_ID";
        final QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        final QueueItem ITEM2 = mock(QueueItem.class);
        final QueueItem LAST_ITEM = mock(QueueItem.class);
        MediaDescriptionCompat description = mock(MediaDescriptionCompat.class);
        when(description.getMediaId()).thenReturn(MEDIA_ID);
        when(LAST_ITEM.getDescription()).thenReturn(description);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.onAddQueueItem(LAST_ITEM);
        playbackManager.setCurrentItem(MEDIA_ID);

        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(LAST_ITEM, result);
    }
    /**
     * GIVEN: a playlist of one item with a media id 'm'
     * WHEN: we call getCurrentMediaId()
     * THEN: m is returned
     */
    @Test
    public void testGetCurrentMediaId() {
        final String MEDIA_ID = "MEDIA_ID";
        final QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        MediaDescriptionCompat description = mock(MediaDescriptionCompat.class);
        when(description.getMediaId()).thenReturn(MEDIA_ID);
        when(ITEM1.getDescription()).thenReturn(description);
        playbackManager.onAddQueueItem(ITEM1);
        final String result = playbackManager.getCurrentMediaId();
        assertEquals(MEDIA_ID, result);
    }
    /**
     * GIVEN: an empty playlist
     * WHEN: we call getCurrentMediaId()
     * THEN: null is returned
     */
    @Test
    public void testGetCurrentMediaIdWithNullPlaylist() {
        final String result = playbackManager.getCurrentMediaId();
        assertNull(result);
    }
    /**
     * GIVEN: a playlist of 2 items where the current position is the first in the queue
     * WHEN: skipToNext() is called
     * THEN: the current item is the second in the queue
     */
    @Test
    public void testSkipToNext() {
        QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.skipToNext();
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(ITEM2, result);
    }
    /**
     * GIVEN: a playlist of 2 items where the current position is the last in the queue
     * WHEN: skipToNext() is called
     * THEN: the current item is the first in the queue
     */
    @Test
    public void testSkipToNextLastItemWhenRepeating() {
        QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.setRepeating(true);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.notifyPlaybackComplete(); // make current item the last item
        playbackManager.skipToNext();
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(ITEM1, result);
    }
    /**
     * GIVEN: a playlist of 2 items where the current position is the last in the queue
     * WHEN: skipToNext() is called
     * THEN: the current item is unchanged and still the last in the queue
     */
    @Test
    public void testSkipToNextLastItemWhenNotRepeating() {
        QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.setRepeating(false);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.notifyPlaybackComplete(); // make current item the last item
        playbackManager.skipToNext();
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(ITEM2, result);
    }
    /**
     * GIVEN: a playlist of 2 items where the current position is the second in the queue
     * WHEN: skipToPrevious() is called
     * THEN: the current item is the first in the queue
     */
    @Test
    public void testSkipToPrevious() {
        QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.notifyPlaybackComplete(); // make current item the second in the queue
        playbackManager.skipToPrevious();
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(ITEM1, result);
    }
    /**
     * GIVEN: a playlist of 2 items where the current position is the first in the queue
     * WHEN: skipToPrevious() is called
     * THEN: the current item is the last in the queue
     */
    @Test
    public void testSkipToPreviousFirstItemWhenRepeating() {
        QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.setRepeating(true);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.skipToPrevious();
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(ITEM2, result);
    }
    /**
     * GIVEN: a playlist of 2 items where the current position is the first in the queue
     * WHEN: skipToPrevious() is called
     * THEN: the current item is unchanged and still the first in the queue
     */
    @Test
    public void testSkipToPreviousFirstItemWhenNotRepeating() {
        QueueItem ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem ITEM2 = mock(QueueItem.class);
        playbackManager.setRepeating(false);
        playbackManager.onAddQueueItem(ITEM1);
        playbackManager.onAddQueueItem(ITEM2);
        playbackManager.skipToPrevious();
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(ITEM1, result);
    }
    /**
     * GIVEN: a playback manager with 2 items in the playlist
     * WHEN: createNewPlaylist() is called with a new list of 1 item
     * THEN: the new playbackManager queue size is 1
     */
    @Test
    public void testCreateNewPlaylist()
    {
        final int OLD_QUEUE_SIZE = 2;
        QueueItem OLD_ITEM1 = MOCK_QUEUE_ITEM;
        QueueItem OLD_ITEM2 = mock(QueueItem.class);
        playbackManager.onAddQueueItem(OLD_ITEM1);
        playbackManager.onAddQueueItem(OLD_ITEM2);
        assertEquals(OLD_QUEUE_SIZE, playbackManager.getQueueSize());
        final int NEW_QUEUE_SIZE = 1;
        QueueItem NEW_ITEM = mock(QueueItem.class);
        List<QueueItem> newList = new ArrayList<>();
        newList.add(NEW_ITEM);
        playbackManager.createNewPlaylist(newList);
        assertEquals(NEW_QUEUE_SIZE, playbackManager.getQueueSize());
        QueueItem result = playbackManager.getCurrentItem();
        assertEquals(NEW_ITEM, result);
    }
    /**
     * GIVEN: a playback manager with a queue size of 11
     * WHEN: we call shuffleNewIndex()
     * THEN: it returns a number between 0 and 10 inclusive
     */
    @Test
    public void testShuffleNewIndex() {
        final int QUEUE_SIZE = 11;
        PlaybackManager playbackManager = mock(PlaybackManager.class);
        when(playbackManager.getQueueSize()).thenReturn(QUEUE_SIZE);
        when(playbackManager.shuffleNewIndex()).thenCallRealMethod();
        int result = playbackManager.shuffleNewIndex();
        System.out.println("number generated: " + result);
        assertTrue(result >= 0);
        assertTrue(result < QUEUE_SIZE);
    }
}