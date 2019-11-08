package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter;

import java.util.List;

public abstract class ContentSearcher {

    protected final ResultsFilter resultsFilter;
    public abstract List<MediaItem> search(@NonNull String query);
    public abstract MediaItemType getSearchCategory();

    protected ContentSearcher(ResultsFilter resultsFilter) {
        this.resultsFilter = resultsFilter;
    }
    public boolean isFilterable() {
        return resultsFilter != null;
    }
}
