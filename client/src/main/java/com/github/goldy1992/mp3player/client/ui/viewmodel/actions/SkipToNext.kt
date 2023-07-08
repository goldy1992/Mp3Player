package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import kotlinx.coroutines.launch

interface SkipToNext : MediaViewModelBase {

    fun skipToNext() {
        scope.launch { mediaRepository.skipToNext() }
    }
}