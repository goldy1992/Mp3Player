package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.player.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
constructor(
    val mediaBrowserAdapter: MediaBrowserAdapter,
    val mediaControllerAdapter: MediaControllerAdapter,
    val playbackParametersFlow: PlaybackParametersFlow,
    val isPlayingFlow: IsPlayingFlow,
    val metadataFlow: MetadataFlow,
    val repeatModeFlow: RepeatModeFlow,
    val shuffleModeFlow: ShuffleModeFlow,
    val queueFlow: QueueFlow,
    val playbackSpeedFlow: PlaybackSpeedFlow
) : ViewModel() {
}