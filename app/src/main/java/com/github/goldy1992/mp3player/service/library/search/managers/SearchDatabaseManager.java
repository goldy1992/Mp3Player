package com.github.goldy1992.mp3player.service.library.search.managers;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

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

    abstract T createFromMediaItem(@NonNull MediaItem item);

    void reindex() {
        handler.post(() -> {
            List<MediaItem> results = contentManager.getChildren(rootCategoryId);
            List<T> entries = buildResults(results);
            deleteOld(entries);
            // replace any new entries
            dao.insertAll(entries);
        });
    }

    private void deleteOld(List<T> entries) {
        // Delete old entries i.e. files that have been deleted.
        List<String> ids = new ArrayList<>();
        for (T entry : entries) {
            ids.add(entry.getId());
        }
        // remove old entries
        dao.deleteOld(ids);
    }

    private List<T> buildResults(List<MediaItem> mediaItems) {
        List<T> entries = new ArrayList<>();
        for (MediaItem mediaItem : mediaItems) {
            T entry = createFromMediaItem(mediaItem);
            entries.add(entry);
        }
        return entries;
    }
}
