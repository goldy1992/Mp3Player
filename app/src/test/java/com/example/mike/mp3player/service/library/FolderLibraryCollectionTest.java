package com.example.mike.mp3player.service.library;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FolderLibraryCollectionTest {
    @Mock
    private MediaBrowserCompat.MediaItem mockMediaItem;

    private FolderLibraryCollection folderLibraryCollection;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        folderLibraryCollection = new FolderLibraryCollection();
    }

    @Test
    public void testIndexWithNullList() {
        List<MediaBrowserCompat.MediaItem> mediaItems = null;
        folderLibraryCollection.index(mediaItems);

        assertTrue(folderLibraryCollection.collection.isEmpty(), "Folders collection should be empty");
    }

    @Test
    public void testIndex() {
        final int EXPECTED_NUMBER_OF_FOLDERS = 2;
        final int EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER1 = 2;
        final int EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER2 = 1;
        final String FOLDER1 = "folder1";
        final String PATH1 = "/c/" + FOLDER1;
        final String FOLDER2 = "folder2";
        final String PATH2 = "/c/" + FOLDER2;
        MediaBrowserCompat.MediaItem song1 = createMediaItemWithFolderRef("mp3_1.mp3", FOLDER1, PATH1);
        MediaBrowserCompat.MediaItem song2 = createMediaItemWithFolderRef("mp3_2.mp3", FOLDER1, PATH1);
        MediaBrowserCompat.MediaItem song3 = createMediaItemWithFolderRef("mp3_3.mp3", FOLDER2, PATH2);

        List<MediaBrowserCompat.MediaItem> items =new ArrayList<>();
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

        
    }

    private MediaBrowserCompat.MediaItem createMediaItemWithFolderRef(String name, String parentDirectory, String parentPath) {
        Bundle extras = mock(Bundle.class);
        when(extras.getString(META_DATA_PARENT_DIRECTORY_PATH)).thenReturn(parentPath);
        when(extras.getString(META_DATA_PARENT_DIRECTORY_NAME)).thenReturn(parentDirectory);
        MediaDescriptionCompat descr = new MediaDescriptionCompat.Builder()
                .setExtras(extras)
                .setTitle(name)
                .build();
        return new MediaBrowserCompat.MediaItem(descr, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }
}