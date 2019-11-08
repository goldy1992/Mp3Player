package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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

    static List<MediaItem> expectedResult = new ArrayList<>();


    static {
        expectedResult.add(mock(MediaItem.class));
    }

    public abstract void testGetMediaType();

    public abstract void testSearchValidMultipleArguments();

    @Test
    public void testSearchInvalid() {
        when(searcher.resultsParser.create(eq(any()), idPrefix)).thenReturn(expectedResult);
        List<?> result = searcher.search(INVALID_QUERY);
        assertNotEquals(expectedResult, result);
    }

}
