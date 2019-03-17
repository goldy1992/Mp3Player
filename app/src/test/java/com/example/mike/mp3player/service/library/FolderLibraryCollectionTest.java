package com.example.mike.mp3player.service.library;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * FolderLibraryCollectionTest class
 */
public class FolderLibraryCollectionTest {
    @Mock
    private MediaItem mockMediaItem;
    final String FOLDER1 = "folder1";
    final String PATH1 = "/c/" + FOLDER1;
    final String FOLDER2 = "folder2";
    final String PATH2 = "/c/" + FOLDER2;
    final String PATH3 ="/d/" + FOLDER2;
    final String MP3_1 = "mp3_1.mp3";
    final String MP3_2 = "mp3_2.mp3";
    final String MP3_3 = "mp3_3.mp3";
    final int EXPECTED_NUMBER_OF_FOLDERS = 2;
    final int EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER1 = 2;
    final int EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER2 = 1;


    private FolderLibraryCollection folderLibraryCollection;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        folderLibraryCollection = new FolderLibraryCollection();
    }

    /**
     * GIVEN: a NULL list which that is to be indexed on an EMPTY FolderCollection
     * WHEN: the indexer processes the null list
     * THEN: The FolderCollection size is 0 and no error is thrown.
     */
    @Test
    public void testIndexWithNullList() {
        List<MediaItem> mediaItems = null;
        folderLibraryCollection.index(mediaItems);

        assertTrue(folderLibraryCollection.collection.isEmpty(), "Folders collection should be empty");
    }
    /**
     * GIVEN: a list of 2 media items of 2 different folders of which 2 of them have the same folder name
     * WHEN: the indexer processes the null list
     * THEN: The FolderCollection size is 3 and no error is thrown.
     */
    @Test
    public void testIndexWithTwoFoldersOfSameTitleList() {
        List<MediaItem> itemsToIndex = new ArrayList<>();
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_1, FOLDER1, PATH1));
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_3, FOLDER2, PATH2));
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_3, FOLDER2, PATH3));
        indexTestData(itemsToIndex);

        assertEquals(3, folderLibraryCollection.collection.keySet().size(), "Folders collection size should be 3");
    }
    /**
     * GIVEN: @See{indexTestData()}
     * WHEN: The indexer processes the directories
     * THEN: Both directories are added to the collection
     * AND: FOLDER1 is added before FOLDER2 => directories are added in the order of their folder name.
     */
    @Test
    public void testIndexAndOrdering() {
        List<MediaItem> itemsToIndex = new ArrayList<>();
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_1, FOLDER1, PATH1));
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_2, FOLDER1, PATH1));
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_3, FOLDER2, PATH2));
        indexTestData(itemsToIndex);
        assertEquals(EXPECTED_NUMBER_OF_FOLDERS, folderLibraryCollection.collection.keySet().size());
        assertEquals(EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER1, folderLibraryCollection.collection.get(PATH1).size());
        assertEquals(EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER2, folderLibraryCollection.collection.get(PATH2).size());

        // assert order
        List<String> collectionKeysetAsList = new ArrayList<>(folderLibraryCollection.collection.keySet());
        assertEquals(PATH1, collectionKeysetAsList.get(0));
        assertEquals(PATH2, collectionKeysetAsList.get(1));
    }

    /**
     * GIVEN: @See{indexTestData()}
     * WHEN: FOLDER1's children are requested from the FolderCollection
     * THEN: It contains a list of 2 songs: mp3_1.mp3 and mp3_2.mp3
     */
    @Test
    public void testGetChildren() {
        List<MediaItem> itemsToIndex = new ArrayList<>();
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_1, FOLDER1, PATH1));
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_2, FOLDER1, PATH1));
        itemsToIndex.add(createMediaItemWithFolderRef(MP3_3, FOLDER2, PATH2));
        indexTestData(itemsToIndex);

        LibraryId libraryId = new LibraryId(Category.FOLDERS, PATH1);
        TreeSet<MediaItem> result = folderLibraryCollection.getChildren(libraryId);
        StringBuilder errorMessage = new StringBuilder().append("Number of items in FOLDER1 should be 2 but was: ")
                .append(result.size());
        assertEquals(EXPECTED_NUMBER_OF_TRACKS_IN_FOLDER1, result.size(), errorMessage.toString());
        List<MediaItem> resultList = new ArrayList<>(result);
        MediaItem firstChild = resultList.get(0);
        String firstChildTitle = firstChild.getDescription().getTitle().toString();
        errorMessage = new StringBuilder().append("First child should have title ")
                .append(MP3_1).append(" but was: ").append(firstChildTitle);
        assertEquals(MP3_1, firstChildTitle, errorMessage.toString());

        MediaItem secondChild = resultList.get(1);
        String secondChildTitle = secondChild.getDescription().getTitle().toString();
        errorMessage = new StringBuilder().append("Second child should have title ")
                .append(MP3_2).append(" but was: ").append(secondChildTitle);
        assertEquals(MP3_2, secondChildTitle, errorMessage.toString());
    }
    /**
     * Utility method to create a MediaItem given:
     * @param name the file name
     * @param parentDirectory the file's parent directory name
     * @param parentPath the file's parent directory path
     * @return the resulting MediaItem object
     */
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
    /**
     * GIVEN: 2 directories with the same parent, FOLDER1 and FOLDER2, with children that are music
     * files.
     * AND: FOLDER1 contains mp3_1.mp3 and mp3_2.mp3
     * AND FOLDER2 contains mp3_3.mp3
     */
    private void indexTestData(List<MediaItem> items) {
        folderLibraryCollection.index(items);
    }
}