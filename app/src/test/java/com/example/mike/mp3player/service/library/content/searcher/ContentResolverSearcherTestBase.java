package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.service.library.content.parser.ResultsParser;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class ContentResolverSearcherTestBase<T extends ContentResolverSearcher> {

    T searcher;
    static final String idPrefix = "idPrefix";

    static final String VALID_QUERY = "VALID_QUERY";
    static final String INVALID_QUERY = "INVALID_QUERY";

    @Mock
    ContentResolver contentResolver;

    @Mock
    Cursor cursor;

    @Mock
    ResultsParser resultsParser;

    static List<MediaBrowserCompat.MediaItem> expectedResult = new ArrayList<>();

    static {
        expectedResult.add(mock(MediaBrowserCompat.MediaItem.class));
    }

    public void setup(){
        MockitoAnnotations.initMocks(this);

    }

    public abstract void testGetMediaType();

    @Test
    public void testSearchValid() {
        when(resultsParser.create(null, idPrefix)).thenReturn(expectedResult);
        List<MediaBrowserCompat.MediaItem> result = searcher.search(VALID_QUERY);
        verify(searcher, times(1)).getProjection();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchInvalid() {
        when(searcher.resultsParser.create(eq(any()), idPrefix)).thenReturn(expectedResult);
        List<MediaBrowserCompat.MediaItem> result = searcher.search(INVALID_QUERY);
        verify(searcher, times(1)).getProjection();
        assertNotEquals(expectedResult, result);
    }

}
