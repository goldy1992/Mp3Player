package com.github.goldy1992.mp3player.client.ui.screens.search

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.ui.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils.isEmpty
import org.apache.commons.lang3.StringUtils.isNotEmpty
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel
    @Inject
    constructor(
        val mediaBrowserAdapter: MediaBrowserAdapter,
        val mediaControllerAdapter: MediaControllerAdapter,
        private val onSearchResultsChangedFlow: OnSearchResultsChangedFlow,
        private val isPlayingFlow: IsPlayingFlow,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher)

    : ViewModel(), LogTagger {

    val searchQuery : StateFlow<String> = mediaBrowserAdapter.currentSearchQuery

    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            if (isEmpty(query)) {
                _searchResults.value = emptyList()
            }
            mediaBrowserAdapter.search(query, Bundle())
            Log.i(logTag(), "New searchQueryValue: ${query}")
        }
    }

    private val _searchResults : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val searchResults : StateFlow<List<MediaItem>> = _searchResults
    init {
        viewModelScope.launch {
            onSearchResultsChangedFlow.flow.collect {
                if (isNotEmpty(searchQuery.value) && it.itemCount > 0) {
                    val results = mediaBrowserAdapter.getSearchResults(it.query, 0, it.itemCount)
                    _searchResults.value = results
                } else {
                    _searchResults.value = emptyList()
                    Log.i(logTag(), "No search results returned")
                }
            }
        }
    }


    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch {
            _isPlayingState.value = mediaControllerAdapter.isPlaying()
        }
        viewModelScope.launch {
            isPlayingFlow.flow().collect {
                _isPlayingState.value = it
            }
        }
    }

    override fun logTag(): String {
        return "SrchScrnViewModel"
    }
}