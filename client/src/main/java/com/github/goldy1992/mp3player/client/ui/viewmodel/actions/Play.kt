package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.util.Log
import kotlinx.coroutines.launch

interface Play : MediaViewModelBase {
    companion object {
        const val LOG_TAG = "Play"
    }
    fun play() {
        scope.launch {
            Log.v(LOG_TAG, "play() invoked")
            mediaRepository.play() }
    }
}