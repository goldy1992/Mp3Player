package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import com.github.goldy1992.mp3player.client.models.RepeatMode
import kotlinx.coroutines.launch

interface SetRepeatMode : MediaViewModelBase {

    fun setRepeatMode(repeatMode: RepeatMode) {
        scope.launch {
            mediaRepository.setRepeatMode(repeatMode) }
    }
}