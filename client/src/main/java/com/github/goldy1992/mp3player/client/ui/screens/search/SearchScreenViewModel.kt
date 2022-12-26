package com.github.goldy1992.mp3player.client.ui.screens.search

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils.isEmpty
import org.apache.commons.lang3.StringUtils.isNotEmpty
import javax.inject.Inject

/**
 * [ViewModel] implementation from the [SearchScreen].
 */
@HiltViewModel
class SearchScreenViewModel
    @Inject
    constructor(
        private val mediaRepository: MediaRepository
    )

    : ViewModel(), LogTagger {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery

    init {
        viewModelScope.launch {
            mediaRepository.currentSearchQuery()
                .collect {
                    _searchQuery.value = it
                }
        }
    }

    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            if (isEmpty(query)) {
                _searchResults.value = emptyList()
            }
            mediaRepository.search(query, Bundle())
            Log.i(logTag(), "New searchQueryValue: ${query}")
        }
    }

    private val _searchResults : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val searchResults : StateFlow<List<MediaItem>> = _searchResults
    init {
        viewModelScope.launch {
            mediaRepository
            .onSearchResultsChanged()
            .collect {
                if (isNotEmpty(searchQuery.value) && it.itemCount > 0) {

                    val results = mediaRepository.getSearchResults(it.query, 0, it.itemCount)
                    _searchResults.value = results
                    Log.i(logTag(), "got search results ${results}")
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
            mediaRepository.isPlaying().
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
            mediaRepository.play(mediaItem)
        }
    }

    fun playFromList(itemIndex : Int, mediaItemList : List<MediaItem>) {

    }

    fun play() {
        viewModelScope.launch { mediaRepository.play() }
    }

    fun pause() {
        viewModelScope.launch { mediaRepository.play() }
    }

    fun skipToNext() {
        viewModelScope.launch { mediaRepository.skipToNext() }
    }

    fun skipToPrevious() {
        viewModelScope.launch { mediaRepository.skipToPrevious() }
    }


    override fun logTag(): String {
        return "SrchScrnViewModel"
    }
}