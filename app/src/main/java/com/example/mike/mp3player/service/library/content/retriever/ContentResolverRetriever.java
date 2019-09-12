package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import java.util.List;

public abstract class ContentResolverRetriever extends ContentRetriever {

    final ContentResolver contentResolver;
    final ResultsParser resultsParser;
    final SearchDatabase searchDatabase;

    public ContentResolverRetriever(ContentResolver contentResolver, ResultsParser resultsParser, SearchDatabase searchDatabase) {
        super();
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
        this.searchDatabase = searchDatabase;
    }

    abstract Cursor performGetChildrenQuery(String id);
    abstract void updateSearchDatabase(List<MediaItem> results);
    abstract String[] getProjection();

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getQueryString());
        List<MediaItem> results = resultsParser.create(cursor, request.getMediaIdPrefix());
        updateSearchDatabase(results);
        return results;
    }
}
