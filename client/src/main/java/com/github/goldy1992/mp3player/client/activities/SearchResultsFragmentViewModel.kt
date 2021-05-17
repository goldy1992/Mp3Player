package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.callbacks.search.SearchResultListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] class for
 * [com.github.goldy1992.mp3player.client.views.fragments.SearchResultsFragment].
 */
@HiltViewModel
class SearchResultsFragmentViewModel

    @Inject
    constructor() : ViewModel(), SearchResultListener {

    var searchResults : MutableLiveData<List<MediaItem>> = MutableLiveData()

    var currentQuery : String = ""

    /**
     * Sets the value of [SearchResultsFragmentViewModel.searchResults] with the parameter
     * [searchResults].
     */
    override fun onSearchResult(searchResults: List<MediaItem>?) {
        this.searchResults.postValue(searchResults)
    }
}