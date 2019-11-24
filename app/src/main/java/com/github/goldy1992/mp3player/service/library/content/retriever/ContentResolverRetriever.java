package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.search.SearchDao;
import com.github.goldy1992.mp3player.service.library.search.SearchEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class ContentResolverRetriever<T extends SearchEntity> extends ContentRetriever {

    final ContentResolver contentResolver;
    final ResultsParser resultsParser;
    final SearchDao<T> dao;
    final Handler handler;

    public ContentResolverRetriever(ContentResolver contentResolver, ResultsParser resultsParser, SearchDao<T> dao, Handler handler) {
        super();
        this.contentResolver = contentResolver;
        this.resultsParser = resultsParser;
        this.dao = dao;
        this.handler = handler;
    }

    abstract Cursor performGetChildrenQuery(String id);
    void updateSearchDatabase(List<MediaItem> results) {
        if (isSearchable()) {
            handler.post(() -> {
                final int resultsSize = results.size();
                final int count = dao.getCount();

                if (count != resultsSize) { // INSERT NORMALISED VALUES
                    List<T> entries = new ArrayList<>();
                    for (MediaBrowserCompat.MediaItem mediaItem : results) {
                        T entry = createFromMediaItem(mediaItem);
                        entries.add(entry);
                    }
                    dao.insertAll(entries);
                }
            });
        }
    }
    abstract String[] getProjection();
    abstract T createFromMediaItem(@NonNull MediaItem item);
    abstract boolean isSearchable();

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getQueryString());
        List<MediaItem> results = resultsParser.create(cursor, request.getMediaIdPrefix());
        updateSearchDatabase(results);
        return results;
    }
}
