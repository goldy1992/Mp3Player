package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.viewmodel.MediaViewModel
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.ChangePlaybackSpeed
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SeekTo
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SetRepeatMode
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SetShuffleEnabled
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.QueueViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] for the [NowPlayingScreen]
 */
@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
    constructor(
        mediaRepository: MediaRepository,
    ) : ChangePlaybackSpeed, SeekTo, SetShuffleEnabled, SetRepeatMode,
    MediaViewModel(mediaRepository) {

    val queue = QueueViewModelState(mediaRepository, viewModelScope)

    override fun logTag(): String {
        return "NowPlayingViewModel"
    }
}