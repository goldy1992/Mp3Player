package com.github.goldy1992.mp3player.service.library.content.parser;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SONG;
import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * SongResultsParser test
 */
@RunWith(RobolectricTestRunner.class)
public class SongResultsParserTest extends ResultsParserTestBase {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final String COMMON_TITLE = "a common title";
    private MediaItem expectedMediaItem1;
    private final long ALBUM_ID_1 = 2334L;
    private final String MEDIA_ID_1 = "id1";
    private MediaItem expectedMediaItem2;
    private final long ALBUM_ID_2 = 9268L;
    private final String ID_PREFIX = "sdfa";
    private final String MEDIA_ID_2 = "id2";

    @Before
    public void setup() {
        try {
            File mediaItem1 = temporaryFolder.newFile("uri1");
            File mediaItem2 = temporaryFolder.newFile("uri2");
            expectedMediaItem1 = new MediaItemBuilder(MEDIA_ID_1)
                    .setMediaUri(Uri.parse(mediaItem1.getAbsolutePath()))
                    .setLibraryId(ID_PREFIX + ID_SEPARATOR + MEDIA_ID_1)
                    .setDuration(23423L)
                    .setArtist("artist1")
                    .setTitle(COMMON_TITLE)
                    .setFileName("fileName1")
                    .setAlbumArtUri(Uri.parse(ALBUM_ID_1 + ""))
                    .build();

            expectedMediaItem2 = new MediaItemBuilder(MEDIA_ID_2)
                    .setMediaUri(Uri.parse(mediaItem2.getAbsolutePath()))
                    .setLibraryId(ID_PREFIX + ID_SEPARATOR + MEDIA_ID_2)
                    .setDuration(96406L)
                    .setArtist("artist1")
                    .setTitle(COMMON_TITLE)
                    .setFileName("fileName2")
                    .setAlbumArtUri(Uri.parse(ALBUM_ID_2 + ""))
                    .build();
            this.resultsParser = new SongResultsParser();

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Override
    @Test
    public void testGetType() {
        assertEquals(SONG, resultsParser.getType());
    }

    /**
     * GIVEN: a cursor with 2 results
     * AND: given that both results have the same title
     * WHEN: the parser is run
     * THEN: a list of MediaItems is returned with the expected values
     * AND: the list is of size 2
     */
    @Test
    public void testCreate() {
        List<MediaItem> mediaItems = getResultsForProjection(SONG_PROJECTION.toArray(new String[0]), ID_PREFIX);
        assertEquals(MediaItemUtils.getTitle(expectedMediaItem1), MediaItemUtils.getTitle(mediaItems.get(0)));
        assertEquals(MediaItemUtils.getArtist(expectedMediaItem1), MediaItemUtils.getArtist(mediaItems.get(0)));
        assertEquals(MediaItemUtils.getDuration(expectedMediaItem1), MediaItemUtils.getDuration(mediaItems.get(0)));
        //assertEquals(MediaItemUtils.getAlbumArtUri(expectedMediaItem1), MediaItemUtils.getAlbumArtUri(mediaItems.get(0)));
        assertEquals(MediaItemUtils.getLibraryId(expectedMediaItem1), MediaItemUtils.getLibraryId(mediaItems.get(0)));
        assertEquals(MediaItemUtils.getTitle(expectedMediaItem2), MediaItemUtils.getTitle(mediaItems.get(1)));
        assertEquals(MediaItemUtils.getArtist(expectedMediaItem2), MediaItemUtils.getArtist(mediaItems.get(1)));
        assertEquals(MediaItemUtils.getDuration(expectedMediaItem2), MediaItemUtils.getDuration(mediaItems.get(1)));
        //assertEquals(MediaItemUtils.getAlbumArtUri(expectedMediaItem2), MediaItemUtils.getAlbumArtUri(mediaItems.get(1)));
        assertEquals(MediaItemUtils.getLibraryId(expectedMediaItem2), MediaItemUtils.getLibraryId(mediaItems.get(1)));
    }

    @Override
    Object[][] createDataSet() {
        Object[][] dataSet = new Object[2][];
        dataSet[0] = new Object[] {MediaItemUtils.getMediaUri(expectedMediaItem1),
                MediaItemUtils.getDuration(expectedMediaItem1),
                MediaItemUtils.getArtist(expectedMediaItem1),
                MediaItemUtils.getMediaId(expectedMediaItem1),
                MediaItemUtils.getTitle(expectedMediaItem1),
                ALBUM_ID_1};

        dataSet[1] = new Object[] {MediaItemUtils.getMediaUri(expectedMediaItem2),
                MediaItemUtils.getDuration(expectedMediaItem2),
                MediaItemUtils.getArtist(expectedMediaItem2),
                MediaItemUtils.getMediaId(expectedMediaItem2),
                MediaItemUtils.getTitle(expectedMediaItem2),
                ALBUM_ID_2};

        return dataSet;
    }
}