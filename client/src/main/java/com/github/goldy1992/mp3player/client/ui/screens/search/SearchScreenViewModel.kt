package com.github.goldy1992.mp3player.client.ui.screens.search

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.SearchResults
import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.client.models.State
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.PlayPlaylist
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.PlaySong
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        override val mediaRepository: MediaRepository
    )
    : Pause, Play, PlayPlaylist, PlaySong, SkipToNext, SkipToPrevious, ViewModel(), LogTagger {

    override val scope = viewModelScope
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
                _searchResults.value = SearchResults.NO_RESULTS
            }
            mediaRepository.search(query, Bundle())
            Log.i(logTag(), "New searchQueryValue: $query")
        }
    }

    private val _searchResults : MutableStateFlow<SearchResults> = MutableStateFlow(SearchResults(
        State.NOT_LOADED))
    val searchResults : StateFlow<SearchResults> = _searchResults
    init {
        viewModelScope.launch {
            mediaRepository
            .onSearchResultsChanged()
            .collect {
                if (isNotEmpty(searchQuery.value) && it.itemCount > 0) {
                    val results = mediaRepository.getSearchResults(it.query, 0, it.itemCount)
                    _searchResults.value = results
                    Log.i(logTag(), "got search results $results")
                } else {
                    _searchResults.value = SearchResults(State.NO_RESULTS)
                    Log.i(logTag(), "No search results returned")
                }

            }
        }
    }

    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)

    fun play(song: Song) {
        viewModelScope.launch {
            mediaRepository.play(song)
        }
    }


    override fun logTag(): String {
        return "SearchScreenViewModel"
    }
}