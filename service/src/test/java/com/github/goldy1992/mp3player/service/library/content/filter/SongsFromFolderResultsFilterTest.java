package com.github.goldy1992.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SongsFromFolderResultsFilterTest {

    private SongsFromFolderResultsFilter songsFromFolderResultsFilter;

    @Before
    public void setup() {
        this.songsFromFolderResultsFilter = new SongsFromFolderResultsFilter();
    }

    @Test
    public void testFilterValidQuery() {
        final String query = "/a/b/c";
        final File expectedDirectory = new File(query);

        MediaItem dontFilter = new MediaItemBuilder("fds")
                .setDirectoryFile(expectedDirectory)
                .build();

        MediaItem toFilter = new MediaItemBuilder("fds")
                .setDirectoryFile(new File("/a/otherDir"))
                .build();

        List<MediaItem> items = new ArrayList<>();
        items.add(dontFilter);
        items.add(toFilter);

        List<MediaItem> results = songsFromFolderResultsFilter.filter(query, items);
        assertEquals(1, results.size());
        assertTrue(results.contains(dontFilter));
        assertFalse(results.contains(toFilter));
    }
}