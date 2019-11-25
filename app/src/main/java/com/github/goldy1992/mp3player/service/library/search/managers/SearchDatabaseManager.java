package com.github.goldy1992.mp3player.service.library.search.managers;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.search.SearchDao;
import com.github.goldy1992.mp3player.service.library.search.SearchEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

public abstract class SearchDatabaseManager<T extends SearchEntity> {

    private final ContentManager contentManager;
    private final Handler handler;
    private final SearchDao<T> dao;
    private final String rootCategoryId;

    public SearchDatabaseManager(ContentManager contentManager,
                                 @Named("worker") Handler handler,
                                 SearchDao<T> dao,
                                 String rootCategoryId) {
        this.contentManager = contentManager;
        this.handler = handler;
        this.dao = dao;
        this.rootCategoryId = rootCategoryId;
    }

    abstract T createFromMediaItem(@NonNull MediaBrowserCompat.MediaItem item);

    void reindex() {
        List<MediaBrowserCompat.MediaItem> results = contentManager.getChildren(rootCategoryId);
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
