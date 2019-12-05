package com.github.goldy1992.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FolderSearchResultsFilterTest {

    private FolderSearchResultsFilter folderSearchResultsFilter;

    @Before
    public void setup() {
        this.folderSearchResultsFilter = new FolderSearchResultsFilter();
    }

    @Test
    public void testFilterTwoItems() {
        final String filterQuery = "abc";
        final int expectedResultsSize = 2;
        File file1ToKeep = new File("/a/b/abc101");
        MediaItem item1Keep = new MediaItemBuilder("id")
                .setDirectoryFile(file1ToKeep)
                .build();

        File file2ToThrow = new File("/a/b/1ac101");
        MediaItem item2Throw = new MediaItemBuilder("id")
                .setDirectoryFile(file2ToThrow)
                .build();

        File file3ToThrow = new File("/a/abc/101");
        MediaItem item3Throw = new MediaItemBuilder("id")
                .setDirectoryFile(file3ToThrow)
                .build();


        File file4ToKeep = new File("/a/abc/abc10abc1");
        MediaItem item4Keep = new MediaItemBuilder("id")
                .setDirectoryFile(file4ToKeep)
                .build();

        List<MediaItem> resultsToProcess = new ArrayList<>();
        resultsToProcess.add(item1Keep);
        resultsToProcess.add(item2Throw);
        resultsToProcess.add(item3Throw);
        resultsToProcess.add(item4Keep);

        List<MediaItem> results = folderSearchResultsFilter.filter(filterQuery, resultsToProcess);
        assertEquals(expectedResultsSize, results.size());
        assertTrue(results.contains(item1Keep));
        assertTrue(results.contains(item4Keep));
    }
}