package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter;
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;
import com.github.goldy1992.mp3player.service.library.search.SearchDao;
import com.github.goldy1992.mp3player.service.library.search.SearchEntity;

import java.util.Collections;
import java.util.List;

public abstract class ContentResolverSearcher<T extends SearchEntity> extends ContentSearcher {
    final ContentResolver contentResolver;
    final ResultsParser resultsParser;
    final SearchDao<T> searchDatabase;

    public ContentResolverSearcher(ContentResolver contentResolver,
                                   ResultsParser resultsParser,
                                   ResultsFilter resultsFilter,
                                   SearchDao<T> searchDatabase) {
        super(resultsFilter);
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
        this.searchDatabase = searchDatabase;
    }

    abstract String getIdPrefix();

    /**
     * @param query the query to search for... assumes that the query as already been normalised
     * @return a list of MediaItem search results
     */
    public List<MediaItem> search(@NonNull String query) {
        Cursor cursor = performSearchQuery(query);

        if(cursor == null) {
            return Collections.emptyList();
        }
        List<MediaItem> results = resultsParser.create(cursor, getIdPrefix());
        if (isFilterable()) {
            /*
             keep in case we need to filter in the future.
             */
        }
        return results;
    }


    abstract String[] getProjection();
    abstract Cursor performSearchQuery(String query);
}
