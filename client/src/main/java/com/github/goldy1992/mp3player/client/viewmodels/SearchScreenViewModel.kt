package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
        val mediaControllerAdapter: MediaControllerAdapter,
        private val onSearchResultsChangedFlow: OnSearchResultsChangedFlow,
        private val isPlayingFlow: IsPlayingFlow,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher)

    : ViewModel(), LogTagger {

    private val mediaControllerAsync : ListenableFuture<MediaController> = mediaControllerAdapter.mediaControllerFuture


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


    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch(mainDispatcher) {
            _isPlayingState.value = mediaControllerAsync.await().isPlaying
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