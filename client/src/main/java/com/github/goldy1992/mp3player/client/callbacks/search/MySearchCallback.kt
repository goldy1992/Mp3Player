package com.github.goldy1992.mp3player.client.callbacks.search

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SearchCallback
import android.util.Log
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MySearchCallback
    @Inject
    constructor()
    : SearchCallback(), LogTagger  {

    private val listeners: MutableSet<SearchResultListener> = mutableSetOf()

    fun registerSearchResultListener(searchResultListener: SearchResultListener) {
        listeners.add(searchResultListener)
    }

    fun unregisterSearchResultListener(searchResultListener: SearchResultListener?): Boolean {
        return listeners.remove(searchResultListener)
    }

    /**
     * {@inheritDoc}
     * @param query the query string
     * @param extras the extras object
     * @param items the list of resulting media items
     */
    override fun onSearchResult(query: String, extras: Bundle?,
                                items: List<MediaBrowserCompat.MediaItem>) {
        Log.i(logTag(), "hit the onSearchResult callback")
        for (listener in listeners) {
            listener.onSearchResult(items)
        }
    }

    override fun logTag(): String {
        return "MY_SRCH_CLBCK"
    }
}