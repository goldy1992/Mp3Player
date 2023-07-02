package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.util.Log
import kotlinx.coroutines.launch

interface Play : MediaViewModelBase {

    fun play() {
        scope.launch {
            Log.v(logTag(), "play() invoked")
            mediaRepository.play() }
    }
}