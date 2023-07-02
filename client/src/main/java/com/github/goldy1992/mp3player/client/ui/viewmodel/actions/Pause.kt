package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.util.Log
import kotlinx.coroutines.launch

interface Pause : MediaViewModelBase {

    fun pause() {
        scope.launch {
            Log.v(logTag(), "play() invoked")
            mediaRepository.pause() }
    }
}