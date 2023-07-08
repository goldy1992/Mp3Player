package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import kotlinx.coroutines.launch

interface Subscribe: MediaViewModelBase {

    fun subscribe(id : String) {
        scope.launch { mediaRepository.subscribe(id) }
    }
}