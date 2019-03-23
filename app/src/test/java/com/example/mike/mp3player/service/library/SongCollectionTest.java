package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.library.LibraryId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class SongCollectionTest {
    @Mock
    private MediaItem mockMediaItem;

    private SongCollection songCollection;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        songCollection = new SongCollection();
    }

    /**
     * GIVEN: A list of 4 MediaItems which are the identical.
     * WHEN: The indexer tries to index the list
     * THEN: Only one file is added to the song set
     */
    @Test
    public void testIndexSongListWithDuplicateValues() {
        List<MediaItem> mediaItems = new ArrayList<>();

        final int EXPECTED_SET_SIZE = 1;

        for (int i = 1; i <= EXPECTED_SET_SIZE; i++) {
            mediaItems.add(mockMediaItem);
        }
        songCollection.index(mediaItems);

        assertNotNull(songCollection.getSongs(), "Songs list is null");
        assertEquals(EXPECTED_SET_SIZE, songCollection.getSongs().size(), "exptected " + EXPECTED_SET_SIZE + " songs but were actually " + songCollection.getSongs().size());
    }

    @Test
    public void testIndexWithEmptyList() {
        List<MediaItem> mediaItems = new ArrayList<>();
        final int EXPECTED_ARRAY_SIZE = 0;
        songCollection.index(mediaItems);
        assertNotNull(songCollection.getSongs(), "Songs list is null");
        assertEquals(EXPECTED_ARRAY_SIZE, songCollection.getSongs().size(), "exptected " + EXPECTED_ARRAY_SIZE + " songs but were actually " + songCollection.getSongs().size());
    }

    /**
     * GIVEN: a null list which needs to be indexed, on a pre-empty SongCollection.
     * WHEN: The indexer processes the list.
     * THEN: An error is NOT thrown and the SongCollection remains empty.
     */
    @Test
    public void testIndexWithNullList() {
        List<MediaItem> mediaItems = null;
        songCollection.index(mediaItems);
        assertTrue(songCollection.getSongs().isEmpty(), "Songs list should be empty");
    }
    /**
     * GIVEN: A SongCollection that is non empty and a LibraryId
     * WHEN: getChildren is called for a LibraryId
     * THEN: null is returned since Songs don't have any children
     */
    @Test
    public void testChildren() {
        List<MediaItem> mediaItems = new ArrayList<>();
        mediaItems.add(mockMediaItem);
        songCollection.index(mediaItems);

        LibraryId libraryId = mock(LibraryId.class);
        Set<MediaItem> resultSet = songCollection.getChildren(libraryId, null);
        assertNull(resultSet);
    }
}