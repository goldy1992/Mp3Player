package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
constructor(
    val mediaBrowserAdapter: MediaBrowserAdapter,
    val mediaControllerAdapter: MediaControllerAdapter,
    val asyncPlayerListener: AsyncPlayerListener,
    val isPlayingFlow: IsPlayingFlow
) : ViewModel() {
}