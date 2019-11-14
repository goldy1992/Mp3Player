package com.github.goldy1992.mp3player.service.library.content.parser;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

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

import static com.github.goldy1992.mp3player.commons.MediaItemType.FOLDER;
import static com.github.goldy1992.mp3player.service.library.content.Projections.FOLDER_PROJECTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * FolderResultsParser test
 */
@RunWith(RobolectricTestRunner.class)
public class FolderResultsParserTest extends ResultsParserTestBase {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File DIR_1;
    private File DIR_2;
    private static final String SONG_1 = "song1";
    private static final String SONG_2 = "song2";
    private File DIR_1_SONG_1;
    private File DIR_1_SONG_2;
    private File DIR_2_SONG_1;
    private final String ID_PREFIX = "sdfgpo";

    @Before
    public void setup() {
        try {
        this.DIR_1 = this.tempFolder.newFolder("dir1");
        this.DIR_2 = this.tempFolder.newFolder("dir2");
        this.DIR_1_SONG_1 = new File(DIR_1, SONG_1);
        this.DIR_1_SONG_1.createNewFile();
        this.DIR_1_SONG_2 = new File(DIR_1, SONG_2);
        this.DIR_1_SONG_2.createNewFile();
        this.DIR_2_SONG_1 = new File(DIR_2, SONG_1);
        this.DIR_2_SONG_1.createNewFile();

        this.resultsParser = new FolderResultsParser();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }


    @Override
    @Test
    public void testGetType() {
        assertEquals(FOLDER, resultsParser.getType());
    }

    @Test
    public void testCreate() {
        List<MediaItem> results = getResultsForProjection(FOLDER_PROJECTION.toArray(new String[0]), ID_PREFIX);
        // I.e one of the items is removed because it is in the same folder as a previous song
        final int expectedResultsSize = 2;
        assertEquals(expectedResultsSize, results.size());

        String item1MediaId = MediaItemUtils.getMediaId(results.get(0));
        assertNotNull(item1MediaId);
        assertTrue(item1MediaId.contains(DIR_1.getName()));
        // ensure songs (child sections of dir path) are removed from directory path
        assertFalse(item1MediaId.contains(SONG_1));

        String item2MediaId = MediaItemUtils.getMediaId(results.get(1));
        assertNotNull(item2MediaId);
        assertTrue(item2MediaId.contains(DIR_2.getName()));
        // ensure songs (child sections of dir path) are removed from directory path
        assertFalse(item2MediaId.contains(SONG_1));

    }

    @Override
    Object[][] createDataSet() {
        Object[][] dataSet = new Object[3][];
        dataSet[0] = new Object[] {DIR_1_SONG_1.getAbsolutePath()};
        dataSet[1] = new Object[] {DIR_1_SONG_2.getAbsolutePath()};
        dataSet[2] = new Object[] {DIR_2_SONG_1.getAbsolutePath()};
        return dataSet;
    }
}
