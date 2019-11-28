package com.github.goldy1992.mp3player.service.library.search.managers;


import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.Folder;
import com.github.goldy1992.mp3player.service.library.search.FolderDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FolderDatabaseManagerTest extends SearchDatabaseManagerTestBase {

    private FolderDatabaseManager folderDatabaseManager;

    private static final String TEST_DIRECTORY_NAME =  "fileName";

    private static final String EXPECTED_DIRECTORY_NAME = TEST_DIRECTORY_NAME.toUpperCase();

    private static final String TEST_DIRECTORY_PATH = File.separator
            + "a" + File.separator
            + "b" + File.separator
            + TEST_DIRECTORY_NAME;

    private static final File TEST_FILE = new File(TEST_DIRECTORY_PATH);



    @Captor
    ArgumentCaptor<Folder> folderCaptor;

    @Captor
    ArgumentCaptor<List<String>> deleteCaptor;

    @Captor
    ArgumentCaptor<List<Folder>> insertAllCaptor;


    @Mock
    private FolderDao folderDao;




    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(searchDatabase.folderDao()).thenReturn(folderDao);
        this.mediaItemTypeIds = new MediaItemTypeIds();
        this.folderDatabaseManager = new FolderDatabaseManager(
                contentManager,
                handler,
                mediaItemTypeIds,
                searchDatabase);
    }

    @Override
    @Test
    public void testInsert() {


        final String expectedId = TEST_FILE.getAbsolutePath();
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build();
        folderDatabaseManager.insert(mediaItem);
        verify(folderDao, times(1)).insert(folderCaptor.capture());
        Folder folder = folderCaptor.getValue();
        assertEquals(expectedId, folder.getId());
        assertEquals(EXPECTED_DIRECTORY_NAME, folder.getValue());
    }

    @Override
    @Test
    public void testReindex() {
        final String expectedId = TEST_FILE.getAbsolutePath();
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build();

        MediaBrowserCompat.MediaItem toReturn = new MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build();
        when(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.FOLDERS)))
                .thenReturn(Collections.singletonList(toReturn));

        folderDatabaseManager.reindex();

        verify(folderDao, times(1)).deleteOld(deleteCaptor.capture());
        List<String> idsToDelete = deleteCaptor.getValue();
        assertEquals(expectedId, idsToDelete.get(0));




    }
}