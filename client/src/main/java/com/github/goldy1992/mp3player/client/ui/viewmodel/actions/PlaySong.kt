package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import com.github.goldy1992.mp3player.client.models.media.Song
import kotlinx.coroutines.launch

interface PlaySong : MediaViewModelBase {

    fun playSong(song: Song) {
        scope.launch { mediaRepository.play(song) }
    }
}