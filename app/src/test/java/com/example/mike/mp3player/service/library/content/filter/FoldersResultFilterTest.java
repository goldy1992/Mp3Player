package com.example.mike.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemBuilder;

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
public class FoldersResultFilterTest {

    private FoldersResultFilter foldersResultFilter;

    @Before
    public void setup() {
        this.foldersResultFilter = new FoldersResultFilter();
    }

    @Test
    public void testFilterTwoItems() {
        final String filterQuery = "abc";
        final int expectedResultsSize = 2;
        File file1ToKeep = new File("/a/b/abc101");
        MediaItem item1Keep = new MediaItemBuilder("id")
                .setFile(file1ToKeep)
                .build();

        File file2ToThrow = new File("/a/b/1ac101");
        MediaItem item2Throw = new MediaItemBuilder("id")
                .setFile(file2ToThrow)
                .build();

        File file3ToThrow = new File("/a/abc/101");
        MediaItem item3Throw = new MediaItemBuilder("id")
                .setFile(file3ToThrow)
                .build();


        File file4ToKeep = new File("/a/abc/abc10abc1");
        MediaItem item4Keep = new MediaItemBuilder("id")
                .setFile(file4ToKeep)
                .build();

        List<MediaItem> resultsToProcess = new ArrayList<>();
        resultsToProcess.add(item1Keep);
        resultsToProcess.add(item2Throw);
        resultsToProcess.add(item3Throw);
        resultsToProcess.add(item4Keep);

        List<MediaItem> results = foldersResultFilter.filter(filterQuery, resultsToProcess);
        assertEquals(expectedResultsSize, results.size());
        assertTrue(results.contains(item1Keep));
        assertTrue(results.contains(item4Keep));
    }
}