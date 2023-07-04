package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentSongViewModelState

    constructor(
        mediaRepository: MediaRepository,
        scope: CoroutineScope)
    : MediaViewModelState<Song>(mediaRepository, scope) {

    private val _currentSongState = MutableStateFlow(Song.DEFAULT)

    init {
        scope.launch {
            mediaRepository.currentSong()
                .collect {
                    _currentSongState.value = it
                }
        }
    }

    override fun state(): StateFlow<Song> {
        return _currentSongState.asStateFlow()
    }

    override fun logTag(): String {
        return "CurrentSongViewModelState"
    }
}