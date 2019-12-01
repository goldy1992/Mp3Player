package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter;
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;

import java.util.List;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final ResultsParser resultsParser;
    final Handler handler;
    final ResultsFilter resultsFilter;

    ContentResolverRetriever(ContentResolver contentResolver,
                                    ResultsParser resultsParser,
                                    Handler handler,
                                    ResultsFilter resultsFilter) {
        super();
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
        this.handler = handler;
        this.resultsFilter = resultsFilter;
    }

    abstract Cursor performGetChildrenQuery(String id);

    abstract String[] getProjection();

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getQueryString());
        List<MediaItem> results =  resultsParser.create(cursor, request.getMediaIdPrefix());
        return null != resultsFilter ? resultsFilter.filter(request.getQueryString(), results) : results;
    }
}
