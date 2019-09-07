package com.example.mike.mp3player.service.library.content.searcher;

import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.filter.ResultsFilter;

import java.util.List;

public abstract class ContentSearcher {

    protected final ResultsFilter resultsFilter;
    public abstract List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
    public abstract MediaItemType getSearchCategory();

    protected ContentSearcher(ResultsFilter resultsFilter) {
        this.resultsFilter = resultsFilter;
    }
    public boolean isFilterable() {
        return resultsFilter != null;
    }
}
