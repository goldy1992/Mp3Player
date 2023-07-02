package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import kotlinx.coroutines.launch

interface SeekTo : MediaViewModelBase {

    fun seekTo(position : Long) {
        scope.launch { mediaRepository.seekTo(position) }
    }
}