package com.github.goldy1992.mp3player.client.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.PlayPlaylist
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Subscribe
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        override val mediaRepository: MediaRepository,
    )  : ViewModel(), Pause, Play, PlayPlaylist, SkipToNext, SkipToPrevious, Subscribe {

    override val scope = viewModelScope
    private val folderId : String = checkNotNull(savedStateHandle["folderId"])
    private val folderName : String = checkNotNull(savedStateHandle["folderName"])
    private val folderPath : String = checkNotNull(savedStateHandle["folderPath"])

    init {
        subscribe(folderId)
    }

    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)

    private val _folder = MutableStateFlow(
        Folder(
            id = folderId,
            name = folderName,
            path = folderPath
        )
    )
    val folder : StateFlow<Folder> = _folder

    init {
        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    replay = 1
                )
                .filter { it.parentId == folderId }
                .collect {
                    _folder.value = mediaRepository.getChildren(folder.value, 0, it.itemCount)
                }
        }
    }

    override fun logTag(): String {
        return "FolderScreenViewModel"
    }
}