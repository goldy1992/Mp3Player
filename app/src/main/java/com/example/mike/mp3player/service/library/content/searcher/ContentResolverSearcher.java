package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.service.library.content.filter.ResultsFilter;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import java.util.List;

public abstract class ContentResolverSearcher extends ContentSearcher {
    final ContentResolver contentResolver;
    final ResultsParser resultsParser;
    final String idPrefix;
    final SearchDatabase searchDatabase;

    public ContentResolverSearcher(ContentResolver contentResolver,
                                   ResultsParser resultsParser,
                                   ResultsFilter resultsFilter,
                                   String idPrefix,
                                   SearchDatabase searchDatabase) {
        super(resultsFilter);
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
        this.idPrefix = idPrefix;
        this.searchDatabase = searchDatabase;
    }

    @Nullable
    public List<MediaItem> search(@NonNull String query) {
        Cursor cursor = performSearchQuery(query);

        if(cursor == null) {
            return null;
        }
        List<MediaItem> results = resultsParser.create(cursor, idPrefix);
        if (isFilterable()) {
            //return resultsFilter.filter(query, results);
        }
        return results;
    }


    abstract String[] getProjection();
    abstract Cursor performSearchQuery(String query);
}
