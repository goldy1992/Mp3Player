package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import com.github.goldy1992.mp3player.client.models.media.Playlist
import kotlinx.coroutines.launch

interface PlayPlaylist : MediaViewModelBase {

    fun playPlaylist(playlist: Playlist, startIndex : Int) {
        scope.launch { mediaRepository.playPlaylist(playlist, startIndex) }
    }
}