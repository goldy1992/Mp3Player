package com.example.mike.mp3player.service.library;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FolderLibraryCollectionTest {
    @Mock
    private MediaItem mockMediaItem;
    final String FOLDER1 = "folder1";
    final String PATH1 = "/c/" + FOLDER1;
    final String FOLDER2 = "folder2";
    final String PATH2 = "/c/" + FOLDER2;

    private FolderLibraryCollection folderLibraryCollection;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        folderLibraryCollection = new FolderLibraryCollection();
    }

    @Test
    public void testIndexWithNullList() {
        List<MediaItem> mediaItems = null;
        folderLibraryCollection.index(mediaItems);

        assertTrue(folderLibraryCollection.collection.isEmpty(), "Folders collection should be empty");
    }

    /**
     * GIVEN: 2 directories with the same parent, FOLDER1 and FOLDER2, with children that are music
     * files.
     * WHEN: TODO: complete decription
     */
    @Test
    public void testIndexAndOrdering() {
        final int EXPECTED_NUMBER_OF_FOLDERS = 2;
        final int EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER1 = 2;
        final int EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER2 = 1;

        MediaItem song1 = createMediaItemWithFolderRef("mp3_1.mp3", FOLDER1, PATH1);
        MediaItem song2 = createMediaItemWithFolderRef("mp3_2.mp3", FOLDER1, PATH1);
        MediaItem song3 = createMediaItemWithFolderRef("mp3_3.mp3", FOLDER2, PATH2);

        List<MediaItem> items =new ArrayList<>();
        items.add(song1);
        items.add(song2);
        items.add(song3);
        
        folderLibraryCollection.index(items);
        assertEquals(EXPECTED_NUMBER_OF_FOLDERS, folderLibraryCollection.collection.keySet().size());
        assertEquals(EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER1, folderLibraryCollection.collection.get(PATH1).size());
        assertTrue(folderLibraryCollection.collection.get(PATH1).contains(song1));
        assertTrue(folderLibraryCollection.collection.get(PATH1).contains(song2));
        assertEquals(EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER2, folderLibraryCollection.collection.get(PATH2).size());
        assertTrue(folderLibraryCollection.collection.get(PATH2).contains(song3));

        // assert order
        List<String> collectionKeysetAsList = new ArrayList<>(folderLibraryCollection.collection.keySet());
        assertEquals(PATH1, collectionKeysetAsList.get(0));
        assertEquals(PATH2, collectionKeysetAsList.get(1));

        
    }

    private MediaItem createMediaItemWithFolderRef(String name, String parentDirectory, String parentPath) {
        Bundle extras = mock(Bundle.class);
        when(extras.getString(META_DATA_PARENT_DIRECTORY_PATH)).thenReturn(parentPath);
        when(extras.getString(META_DATA_PARENT_DIRECTORY_NAME)).thenReturn(parentDirectory);
        MediaDescriptionCompat descr = new MediaDescriptionCompat.Builder()
                .setExtras(extras)
                .setTitle(name)
                .build();
        return new MediaItem(descr, MediaItem.FLAG_BROWSABLE);
    }
}