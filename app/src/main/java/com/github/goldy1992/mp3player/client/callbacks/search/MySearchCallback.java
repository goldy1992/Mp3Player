package com.github.goldy1992.mp3player.client.callbacks.search;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.LogTagger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySearchCallback extends MediaBrowserCompat.SearchCallback implements LogTagger {

    Set<SearchResultListener> listeners;

    public MySearchCallback() {
        this.listeners = new HashSet<>();
    }


    public void registerSearchResultListener(SearchResultListener searchResultListener) {
        this.listeners.add(searchResultListener);
    }

    public boolean unregisterSearchResultListener(SearchResultListener searchResultListener) {
        return this.listeners.remove(searchResultListener);
    }

    /**
     * {@inheritDoc}
     * @param query the query string
     * @param extras the extras object
     * @param items the list of resulting media items
     */
    public void onSearchResult(@NonNull String query, Bundle extras,
                               @NonNull List<MediaBrowserCompat.MediaItem> items) {
        Log.i(getLogTag(), "hit the onSearchResult callback");
        for (SearchResultListener listener : listeners) {
            listener.onSearchResult(items);
        }
    }

    @Override
    public String getLogTag() {
        return "MY_SRCH_CLBCK";
    }
}
