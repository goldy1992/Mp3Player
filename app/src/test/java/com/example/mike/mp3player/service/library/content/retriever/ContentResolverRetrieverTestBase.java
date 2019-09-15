package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

public abstract class ContentResolverRetrieverTestBase<T extends ContentResolverRetriever> {

    T retriever;

    @Mock
    ContentResolver contentResolver;
    @Mock
    ResultsParser resultsParser;
    @Mock
    Cursor cursor;

    Handler handler = new Handler(Looper.getMainLooper());

    ContentRequest contentRequest;

    List<MediaItem> expectedResult = new ArrayList<>();

    public abstract void testGetMediaType();

}
