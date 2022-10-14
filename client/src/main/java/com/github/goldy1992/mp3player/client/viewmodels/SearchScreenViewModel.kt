package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel
    @Inject
    constructor(
        val mediaBrowserAdapter: MediaBrowserAdapter,
        private val onSearchResultsChangedFlow: OnSearchResultsChangedFlow,
        val mediaControllerAdapter: MediaControllerAdapter,
        val asyncPlayerListener: AsyncPlayerListener,
        val isPlayingFlow: IsPlayingFlow) : ViewModel(), LogTagger {


    private val _searchResults : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val searchResults : StateFlow<List<MediaItem>> = _searchResults
    init {
        viewModelScope.launch {
            onSearchResultsChangedFlow.flow.collect {
                if (it.itemCount > 0) {
                    val results = mediaBrowserAdapter.getSearchResults(it.query, 0, it.itemCount)
                    _searchResults.value = results
                } else {
                    Log.i(logTag(), "No search results returned")
                }
            }
        }
    }

    override fun logTag(): String {
        return "SrchScrnViewModel"
    }
}