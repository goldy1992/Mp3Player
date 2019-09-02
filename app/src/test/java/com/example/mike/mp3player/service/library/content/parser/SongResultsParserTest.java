package com.example.mike.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboCursor;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

import static com.example.mike.mp3player.commons.MediaItemType.SONG;
import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;
import static org.junit.Assert.assertEquals;

/**
 * SongResultsParser test
 */
@RunWith(RobolectricTestRunner.class)
public class SongResultsParserTest {

    private SongResultsParser songResultsParser;

    @Before
    public void setup() {
        this.songResultsParser = new SongResultsParser();
    }

    @Test
    public void testGetType() {
        assertEquals(SONG, songResultsParser.getType());
    }

    @Test
    public void testCreate() {
        RoboCursor cursor = new RoboCursor();

        cursor.setColumnNames(Arrays.asList(SONG_PROJECTION));
        cursor.setResults(createDataSet());

        List<MediaItem> mediaItems = songResultsParser.create(cursor, "xyz");
        // TODO: complete rest of assertions
        assertEquals("artist1", MediaItemUtils.extractArtist(mediaItems.get(0)));

    }
    private Object[][] createDataSet() {
        Object[][] dataSet = new Object[1][];
        dataSet[0] = new Object[] {"/a/b/c", 423L, "artist1", "id1", "title1", 24234L};
        return dataSet;
    }
}