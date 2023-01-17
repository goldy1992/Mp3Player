package com.github.goldy1992.mp3player.client.ui.screens.search

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createAlbums
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createFolders
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSong
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSongs
import com.github.goldy1992.mp3player.client.data.SearchResults
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
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
                _searchResults.value = SearchResults.NO_RESULTS
            }
            mediaRepository.search(query, Bundle())
            Log.i(logTag(), "New searchQueryValue: ${query}")
        }
    }

    private val _searchResults : MutableStateFlow<SearchResults> = MutableStateFlow(SearchResults(State.NOT_LOADED))
    val searchResults : StateFlow<SearchResults> = _searchResults
    init {
        viewModelScope.launch {
            mediaRepository
            .onSearchResultsChanged()
            .collect {
                if (isNotEmpty(searchQuery.value) && it.itemCount > 0) {
                    val results = mediaRepository.getSearchResults(it.query, 0, it.itemCount)
                    _searchResults.value = mapResults(results)
                    Log.i(logTag(), "got search results ${results}")
                } else {
                    _searchResults.value = SearchResults(State.NO_RESULTS)
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

    fun play(song: Song) {
        viewModelScope.launch {
            mediaRepository.play(MediaItemBuilder(song.id).build())
        }
    }

    fun playFromList(itemIndex : Int, mediaItemList : Songs) {
        viewModelScope.launch { mediaRepository.playFromSongList(itemIndex = itemIndex, items = mediaItemList.songs.map { MediaItemBuilder(it.id).build() }) }
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

    private fun mapResults(mediaItemList: List<MediaItem>) : SearchResults {
        val songItems : MutableList<MediaItem> = mutableListOf()
        val folderItems : MutableList<MediaItem> = mutableListOf()
        val albumItems : MutableList<MediaItem> = mutableListOf()

        mediaItemList.forEach {
            when (MediaItemUtils.getMediaItemType(it)) {
                MediaItemType.SONG -> songItems.add(it)
                MediaItemType.FOLDER -> folderItems.add(it)
                MediaItemType.ALBUMS -> albumItems.add(it)
                else -> Log.w(logTag(), "Unkown MediaItem returned from search results")
            }
        }

        return SearchResults(
            state = State.LOADED,
            songs = createSongs(State.LOADED, songItems),
            folders = createFolders(State.LOADED, folderItems),
            albums = createAlbums(State.LOADED, albumItems)
        )
    }


    override fun logTag(): String {
        return "SrchScrnViewModel"
    }
}