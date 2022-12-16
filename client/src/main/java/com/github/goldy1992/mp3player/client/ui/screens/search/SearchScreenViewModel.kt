package com.github.goldy1992.mp3player.client.ui.screens.search

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.audiobands.media.browser.MediaBrowserRepository
import com.github.goldy1992.mp3player.client.data.audiobands.media.controller.PlaybackStateRepository
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.ui.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils.isEmpty
import org.apache.commons.lang3.StringUtils.isNotEmpty
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel
    @Inject
    constructor(
        val playbackStateRepository: PlaybackStateRepository,
        val browserRepository: MediaBrowserRepository)
//        val mediaBrowserAdapter: MediaBrowserAdapter,
//        val mediaControllerAdapter: MediaControllerAdapter,
//        private val onSearchResultsChangedFlow: OnSearchResultsChangedFlow,
//        private val isPlayingFlow: IsPlayingFlow,
//        @MainDispatcher private val mainDispatcher: CoroutineDispatcher)

    : ViewModel(), LogTagger {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            browserRepository.currentSearchQuery()
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    replay = 1
                ).collect {
                    _searchQuery.value = it
                }
        }
    }

    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            if (isEmpty(query)) {
                _searchResults.value = emptyList()
            }
            browserRepository.search(query, Bundle())
            Log.i(logTag(), "New searchQueryValue: ${query}")
        }
    }

    private val _searchResults : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val searchResults : StateFlow<List<MediaItem>> = _searchResults
    init {
        viewModelScope.launch {
            browserRepository
            .onSearchResultsChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                if (isNotEmpty(searchQuery.value) && it.itemCount > 0) {
                    val results = browserRepository.getSearchResults(it.query, 0, it.itemCount)
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
            playbackStateRepository.isPlaying().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _isPlayingState.value = it
            }
        }
    }

    fun play(mediaItem: MediaItem) {
        viewModelScope.launch {
            playbackStateRepository.play(mediaItem)
        }
    }

    fun playFromList(itemIndex : Int, mediaItemList : List<MediaItem>) {

    }

    fun play() {
        viewModelScope.launch { playbackStateRepository.play() }
    }

    fun pause() {
        viewModelScope.launch { playbackStateRepository.play() }
    }

    fun skipToNext() {
        viewModelScope.launch { playbackStateRepository.skipToNext() }
    }

    fun skipToPrevious() {
        viewModelScope.launch { playbackStateRepository.skipToPrevious() }
    }


    override fun logTag(): String {
        return "SrchScrnViewModel"
    }
}