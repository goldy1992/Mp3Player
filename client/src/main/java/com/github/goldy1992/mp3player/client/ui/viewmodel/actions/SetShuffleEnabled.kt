package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.util.Log
import kotlinx.coroutines.launch

interface SetShuffleEnabled : MediaViewModelBase {
    companion object
    {
        const val LOG_TAG = "SetShuffleEnabled"
    }
    fun setShuffleEnabled(isEnabled: Boolean) {
        Log.d(LOG_TAG, "setShuffleEnabled() invoked with value: $isEnabled")
        scope.launch { mediaRepository.setShuffleMode(isEnabled) }
    }
}