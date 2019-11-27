package com.github.goldy1992.mp3player.service.library.search.managers;


import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FolderDatabaseManagerTest extends SearchDatabaseManagerTestBase {

    private FolderDatabaseManager folderDatabaseManager;



    @Captor
    ArgumentCaptor<Folder> folderCaptor;


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
        final String directoryName = "fileName";
        final String expectedDirectoryName = directoryName.toUpperCase();


        final File file = new File(File.separator
                + "a" + File.separator
                + "b" + File.separator
                + directoryName);
        final String expectedId = file.getAbsolutePath();
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder(expectedId)
                .setDirectoryFile(file)
                .build();
        folderDatabaseManager.insert(mediaItem);
        verify(folderDao, times(1)).insert(folderCaptor.capture());
        Folder folder = folderCaptor.getValue();
        assertEquals(expectedId, folder.getId());
        assertEquals(expectedDirectoryName, folder.getValue());
    }
}