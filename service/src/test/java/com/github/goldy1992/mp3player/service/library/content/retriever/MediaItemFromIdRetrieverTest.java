package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaItemFromIdRetrieverTest {

    private MediaItemFromIdRetriever mediaItemFromIdRetriever;

    @Mock
    private ContentResolver contentResolver;

    @Mock
    SongResultsParser songResultsParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mediaItemFromIdRetriever = new MediaItemFromIdRetriever(contentResolver, songResultsParser);
    }

    @Test
    public void testNullCursor() {
        final long id = 23L;
        when(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(null);
        MediaItem result = mediaItemFromIdRetriever.getItem(id);
        assertNull(result);
        verify(songResultsParser,  never()).create(any(),any());
    }

    @Test
    public void testGetItemWithResult() {
        final long id = 5464L;
        final Cursor cursor = mock(Cursor.class);
        final MediaItem expectedMediaItem = new MediaItemBuilder("anId")
                .build();
        final List<MediaItem> listToReturn = Collections.singletonList(expectedMediaItem);
        when(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(cursor);
        when(songResultsParser.create(eq(cursor), anyString())).thenReturn(listToReturn);
        MediaItem result = mediaItemFromIdRetriever.getItem(id);
        assertEquals(expectedMediaItem, result);
    }

}