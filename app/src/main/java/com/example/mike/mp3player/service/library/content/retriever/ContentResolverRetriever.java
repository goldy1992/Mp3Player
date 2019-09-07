package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;

import java.util.List;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final ResultsParser resultsParser;

    public ContentResolverRetriever(ContentResolver contentResolver, ResultsParser resultsParser) {
        super();
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
    }

    abstract Cursor performGetChildrenQuery(String id);
    abstract String[] getProjection();

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getQueryString());
        return resultsParser.create(cursor, request.getMediaIdPrefix());
    }
}
