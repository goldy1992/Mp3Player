package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 public class SongCollectionTest {
     @Mock
     private MediaBrowserCompat.MediaItem mockMediaItem;

     private SongCollection songCollection;
     @BeforeEach
     public void setUp() {
         MockitoAnnotations.initMocks(this);
         songCollection = new SongCollection();
     }

     @Test
     public void testIndexWithNonEmptyList() {
         List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

         final int EXPECTED_ARRAY_SIZE = 4;

         for (int i = 1; i <= EXPECTED_ARRAY_SIZE; i++) {
             mediaItems.add(mockMediaItem);
         }
         songCollection.index(mediaItems);

         assertNotNull(songCollection.getSongs(), "Songs list is null");
         assertEquals(EXPECTED_ARRAY_SIZE, songCollection.getSongs().size(), "exptected " + EXPECTED_ARRAY_SIZE + " songs but were actually " + songCollection.getSongs().size());
     }

     @Test
     public void testIndexWithEmptyList() {
         List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

         final int EXPECTED_ARRAY_SIZE = 0;
         songCollection.index(mediaItems);

         assertNotNull(songCollection.getSongs(), "Songs list is null");
         assertEquals(EXPECTED_ARRAY_SIZE, songCollection.getSongs().size(), "exptected " + EXPECTED_ARRAY_SIZE + " songs but were actually " + songCollection.getSongs().size());
     }

     @Test
     public void testIndexWithNullList() {
         List<MediaBrowserCompat.MediaItem> mediaItems = null;
         songCollection.index(mediaItems);
         assertNull(songCollection.getSongs(), "Songs list should be null");
     }

}