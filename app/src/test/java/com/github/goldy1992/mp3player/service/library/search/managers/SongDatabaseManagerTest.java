package com.github.goldy1992.mp3player.service.library.search.managers;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.Song;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner.class)
public class SongDatabaseManagerTest extends SearchDatabaseManagerTestBase {

    private SongDatabaseManager songDatabaseManager;

    @Captor
    ArgumentCaptor<Song> songCaptor;

    @Captor
    ArgumentCaptor<List<String>> deleteCaptor;

    @Captor
    ArgumentCaptor<List<Song>> insertAllCaptor;

    @Mock
    private SongDao songDao;


    @Before
    public void setup() {
        super.setup();
        MockitoAnnotations.initMocks(this);
        when(searchDatabase.songDao()).thenReturn(songDao);
        this.mediaItemTypeIds = new MediaItemTypeIds();
        this.songDatabaseManager = new SongDatabaseManager(
                contentManager,
                handler,
                mediaItemTypeIds,
                searchDatabase);
    }

    @Override
    @Test
    public void testInsert() {
      final String expectedId = "23fsdf";
      final String songTitle = "songTitle";
      final String expectedValue = songTitle.toUpperCase();

        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder(expectedId)
                .setTitle(expectedValue)
                .build();
        songDatabaseManager.insert(mediaItem);
        verify(songDao, times(1)).insert(songCaptor.capture());
        Song song = songCaptor.getValue();
        assertEquals(expectedId, song.getId());
        assertEquals(expectedValue, song.getValue());
    }

    @Test
    @Override
    public void testReindex() {
        final String expectedId = "sdkjdsf";
        final String title = "expectedTitle";
        final String expectedTitle = title.toUpperCase();

        MediaBrowserCompat.MediaItem toReturn = new MediaItemBuilder(expectedId)
                .setTitle(title)
                .build();

        when(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.SONGS)))
                .thenReturn(Collections.singletonList(toReturn));

        songDatabaseManager.reindex();
        shadowOf(handler.getLooper()).idle();

        verify(songDao, times(1)).deleteOld(deleteCaptor.capture());
        List<String> idsToDelete = deleteCaptor.getValue();
        assertEquals(expectedId, idsToDelete.get(0));

        verify(songDao, times(1)).insertAll(insertAllCaptor.capture());
        Song insertedFolder = insertAllCaptor.getValue().get(0);
        assertEquals(expectedId, insertedFolder.getId());
        assertEquals(expectedTitle, insertedFolder.getValue());
    }
}