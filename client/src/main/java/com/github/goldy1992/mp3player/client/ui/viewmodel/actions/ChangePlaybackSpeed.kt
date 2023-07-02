package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import kotlinx.coroutines.launch

interface ChangePlaybackSpeed : MediaViewModelBase {

    fun changePlaybackSpeed(speed : Float) {
        scope.launch {
            mediaRepository.changePlaybackSpeed(speed) }
    }
}