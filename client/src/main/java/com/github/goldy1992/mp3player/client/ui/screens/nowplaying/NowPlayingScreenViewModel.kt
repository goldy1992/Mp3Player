package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.ChangePlaybackSpeed
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SeekTo
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SetRepeatMode
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SetShuffleEnabled
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.PlaybackPositionViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.PlaybackSpeedViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.QueueViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.RepeatModeViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.ShuffleModeViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * [ViewModel] for the [NowPlayingScreen]
 */
@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
    constructor(
        override val mediaRepository: MediaRepository,
    ) : ChangePlaybackSpeed, Pause, Play, SeekTo, SetShuffleEnabled, SetRepeatMode,
    SkipToNext, SkipToPrevious, ViewModel() {

    val playbackPosition = PlaybackPositionViewModelState(mediaRepository, viewModelScope)
    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val playbackSpeed = PlaybackSpeedViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)
    val queue = QueueViewModelState(mediaRepository, viewModelScope)
    val repeatMode = RepeatModeViewModelState(mediaRepository, viewModelScope)
    val shuffleMode = ShuffleModeViewModelState(mediaRepository, viewModelScope)

    override val scope: CoroutineScope = viewModelScope

    override fun logTag(): String {
        return "NowPlayingViewModel"
    }
}