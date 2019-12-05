package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;
import com.github.goldy1992.mp3player.service.library.search.Song;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SONG;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SONGS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SongSearcherTest extends ContentResolverSearcherTestBase<SongSearcher> {

    private MediaItemTypeIds mediaItemTypeIds;

    @Mock
    SongDao songDao;

    @Mock
    SongResultsParser resultsParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mediaItemTypeIds = new MediaItemTypeIds();
        this.idPrefix = mediaItemTypeIds.getId(SONG);
        this.searcher = spy(new SongSearcher(contentResolver, resultsParser, mediaItemTypeIds, songDao));
    }

    @Override
    @Test
    public void testSearchValidMultipleArguments() {
        List<Song> expectedDbResult = new ArrayList<>();
        final String id1 = "id1";
        final String id2 = "id2";
        final String id3 = "id3";
        Song song1 = new Song(id1, "song1");
        Song song2 = new Song(id2, "song2");
        Song song3 = new Song(id3, "song3");
        expectedDbResult.add(song1);
        expectedDbResult.add(song2);
        expectedDbResult.add(song3);
        when(songDao.query(VALID_QUERY)).thenReturn(expectedDbResult);

        final String EXPECTED_WHERE = MediaStore.Audio.Media._ID + " IN(?, ?, ?) COLLATE NOCASE";
        final String[] EXPECTED_WHERE_ARGS = new String[] {id1, id2, id3};
        when(contentResolver.query(EXTERNAL_CONTENT_URI, searcher.getProjection(), EXPECTED_WHERE, EXPECTED_WHERE_ARGS, null))
                .thenReturn(cursor);

        when(resultsParser.create(cursor, idPrefix)).thenReturn(expectedResult);
        List<MediaBrowserCompat.MediaItem> result = searcher.search(VALID_QUERY);
        assertEquals(expectedResult, result);
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONGS, searcher.getSearchCategory());
    }

}