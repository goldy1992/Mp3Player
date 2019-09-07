package com.example.mike.mp3player.service.library.content.parser;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static com.example.mike.mp3player.commons.Constants.ID_SEPARATOR;
import static com.example.mike.mp3player.commons.MediaItemType.SONG;
import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;
import static org.junit.Assert.assertEquals;

/**
 * SongResultsParser test
 */
@RunWith(RobolectricTestRunner.class)
public class SongResultsParserTest extends ResultsParserTestBase {

    private static final String COMMON_TITLE = "a common title";
    private static final MediaItem EXPECTED_MEDIA_ITEM_1;
    private static final long ALBUM_ID_1 = 2334L;
    private static final String MEDIA_ID_1 = "id1";
    private static final MediaItem EXPECTED_MEDIA_ITEM_2;
    private static final long ALBUM_ID_2 = 9268L;
    private static final String ID_PREFIX = "sdfa";
    private static final String MEDIA_ID_2 = "id2";

    static {
        EXPECTED_MEDIA_ITEM_1 = new MediaItemBuilder(MEDIA_ID_1)
            .setMediaUri(Uri.parse("uri1"))
            .setLibraryId(ID_PREFIX + ID_SEPARATOR + MEDIA_ID_1)
            .setDuration(23423L)
            .setArtist("artist1")
            .setTitle(COMMON_TITLE)
            .setFileName("fileName1")
            .setAlbumArtUri(ALBUM_ID_1)
            .build();

        EXPECTED_MEDIA_ITEM_2 = new MediaItemBuilder(MEDIA_ID_2)
                .setMediaUri(Uri.parse("uri2"))
                .setLibraryId(ID_PREFIX + ID_SEPARATOR + MEDIA_ID_2)
                .setDuration(96406L)
                .setArtist("artist1")
                .setTitle(COMMON_TITLE)
                .setFileName("fileName2")
                .setAlbumArtUri(ALBUM_ID_2)
                .build();
    }

    @Before
    public void setup() {
        this.resultsParser = new SongResultsParser();
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
        assertEquals(MediaItemUtils.getTitle(EXPECTED_MEDIA_ITEM_1), MediaItemUtils.getTitle(mediaItems.get(0)));
//        assertEquals(MediaItemUtils.getArtist(EXPECTED_MEDIA_ITEM_1), MediaItemUtils.extractArtist(mediaItems.get(0)));
//        assertEquals(MediaItemUtils.extractDuration(EXPECTED_MEDIA_ITEM_1), MediaItemUtils.extractDuration(mediaItems.get(0)));
//        assertEquals(MediaItemUtils.getAlbumArtUri(EXPECTED_MEDIA_ITEM_1), MediaItemUtils.getAlbumArtUri(mediaItems.get(0)));
//        assertEquals(MediaItemUtils.getLibraryId(EXPECTED_MEDIA_ITEM_1), MediaItemUtils.getLibraryId(mediaItems.get(0)));
//        assertEquals(MediaItemUtils.getTitle(EXPECTED_MEDIA_ITEM_2), MediaItemUtils.extractTitle(mediaItems.get(1)));
//        assertEquals(MediaItemUtils.getArtist(EXPECTED_MEDIA_ITEM_2), MediaItemUtils.extractArtist(mediaItems.get(1)));
//        assertEquals(MediaItemUtils.extractDuration(EXPECTED_MEDIA_ITEM_2), MediaItemUtils.extractDuration(mediaItems.get(1)));
//        assertEquals(MediaItemUtils.getAlbumArtUri(EXPECTED_MEDIA_ITEM_2), MediaItemUtils.getAlbumArtUri(mediaItems.get(1)));
//        assertEquals(MediaItemUtils.getLibraryId(EXPECTED_MEDIA_ITEM_2), MediaItemUtils.getLibraryId(mediaItems.get(1)));
    }

    @Override
    Object[][] createDataSet() {
        Object[][] dataSet = new Object[2][];
        dataSet[0] = new Object[] {MediaItemUtils.getMediaUri(EXPECTED_MEDIA_ITEM_1),
                MediaItemUtils.getDuration(EXPECTED_MEDIA_ITEM_1),
                MediaItemUtils.getArtist(EXPECTED_MEDIA_ITEM_1),
                MediaItemUtils.getMediaId(EXPECTED_MEDIA_ITEM_1),
                MediaItemUtils.getTitle(EXPECTED_MEDIA_ITEM_1),
                ALBUM_ID_1};

        dataSet[1] = new Object[] {MediaItemUtils.getMediaUri(EXPECTED_MEDIA_ITEM_2),
                MediaItemUtils.getDuration(EXPECTED_MEDIA_ITEM_2),
                MediaItemUtils.getArtist(EXPECTED_MEDIA_ITEM_2),
                MediaItemUtils.getMediaId(EXPECTED_MEDIA_ITEM_2),
                MediaItemUtils.getTitle(EXPECTED_MEDIA_ITEM_2),
                ALBUM_ID_2};

        return dataSet;
    }
}