package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;
import com.example.mike.mp3player.service.library.search.SearchDao;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class ContentResolverRetrieverTestBase<T extends ContentResolverRetriever> {

    T retriever;

    @Mock
    ContentResolver contentResolver;
    @Mock
    ResultsParser resultsParser;
    @Mock
    Handler handler;

    ContentRequest contentRequest;

    List<MediaItem> expectedResult = new ArrayList<>();

    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.contentRequest = new ContentRequest("x", "y", "z");
        when(resultsParser.create(eq(any()), contentRequest.getMediaIdPrefix())).thenReturn(expectedResult);
    }

    public abstract void testGetMediaType();

    @Test
    public void testGetChildren() {
        List<MediaBrowserCompat.MediaItem> result = retriever.getChildren(contentRequest);
        verify(retriever, times(1)).getProjection();
        assertEquals(expectedResult, result);
    }
}
