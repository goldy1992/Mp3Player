package com.github.goldy1992.mp3player.client.callbacks.search

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SearchCallback
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySearchCallback
    @Inject
    constructor()
    : SearchCallback(), LogTagger  {

    val searchResults : MutableLiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData()


    /**
     * {@inheritDoc}
     * @param query the query string
     * @param extras the extras object
     * @param items the list of resulting media items
     */
    override fun onSearchResult(query: String, extras: Bundle?,
                                items: List<MediaBrowserCompat.MediaItem>) {
        Log.i(logTag(), "hit the onSearchResult callback")
        searchResults.postValue(items)
    }

    override fun logTag(): String {
        return "MY_SRCH_CLBCK"
    }
}