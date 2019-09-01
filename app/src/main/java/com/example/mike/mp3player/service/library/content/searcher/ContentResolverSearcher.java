package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.service.library.content.filter.ResultsFilter;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;

import java.util.List;

public abstract class ContentResolverSearcher extends ContentSearcher {
    final ContentResolver contentResolver;
    final ResultsParser resultsParser;
    final String idPrefix;
    private ResultsFilter resultsFilter;

    public ContentResolverSearcher(ContentResolver contentResolver,
                                   ResultsParser resultsParser,
                                   String idPrefix) {
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
        this.idPrefix = idPrefix;
    }

    public ContentResolverSearcher(ContentResolver contentResolver,
                                   ResultsParser resultsParser,
                                   String idPrefix,
                                   ResultsFilter resultsFilter) {
        this(contentResolver, resultsParser, idPrefix);
        this.resultsFilter = resultsFilter;
    }

    public List<MediaItem> search(@NonNull String query) {
        Cursor cursor = performSearchQuery(query);
        List<MediaItem> results = resultsParser.create(cursor, idPrefix);
        if (filterable()) {
            return resultsFilter.filter(query, results);
        }
        return results;
    }

    public boolean filterable() {
        return  resultsFilter != null;
    }

    abstract String[] getProjection();
    abstract Cursor performSearchQuery(String query);
}
