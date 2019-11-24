package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

public abstract class ContentResolverRetrieverTestBase<T extends ContentResolverRetriever> {

    T retriever;

    @Mock
    ContentResolver contentResolver;

    @Mock
    SearchDatabase searchDatabase;

    @Mock
    Cursor cursor;

    Handler handler = new Handler(Looper.getMainLooper());

    ContentRequest contentRequest;

    List<MediaItem> expectedResult = new ArrayList<>();

    public abstract void testGetMediaType();

}
