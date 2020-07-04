package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.callbacks.search.SearchResultListener
import javax.inject.Inject

class SearchResultActivityViewModel
    @Inject
    constructor() : ViewModel(), SearchResultListener {

    var searchResults : MutableLiveData<List<MediaItem>> = MutableLiveData()

    override fun onSearchResult(searchResults: List<MediaItem>?) {
        this.searchResults.postValue(searchResults)
    }
}