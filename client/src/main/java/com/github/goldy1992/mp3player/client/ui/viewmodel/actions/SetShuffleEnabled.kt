package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.util.Log
import kotlinx.coroutines.launch

interface SetShuffleEnabled : MediaViewModelBase {

    fun setShuffleEnabled(isEnabled: Boolean) {
        Log.d(logTag(), "setShuffleEnabled() invoked with value: $isEnabled")
        scope.launch { mediaRepository.setShuffleMode(isEnabled) }
    }
}