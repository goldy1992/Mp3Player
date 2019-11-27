package com.github.goldy1992.mp3player.service.library.search.managers;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.Song;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SongDatabaseManagerTest extends SearchDatabaseManagerTestBase {

    private SongDatabaseManager songDatabaseManager;

    @Captor
    ArgumentCaptor<Song> songCaptor;

    @Mock
    private SongDao songDao;


    @Before
    public void setup() {
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
}