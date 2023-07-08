package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import kotlinx.coroutines.launch

interface SkipToPrevious : MediaViewModelBase {

    fun skipToPrevious() {
        scope.launch { mediaRepository.skipToPrevious() }
    }
}