package com.github.goldy1992.mp3player.client.viewmodels.states

import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.data.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.data.flows.player.PlaybackPositionFlow
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.commons.TimerUtils
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@ViewModelScoped
class PlaybackPosition

private constructor(scope: CoroutineScope,
                    playbackPositionFlow: PlaybackPositionFlow,
                    @MainDispatcher dispatcher: CoroutineDispatcher,
                    mediaController : ListenableFuture<MediaController>)
    : PlayerFlowState<PlaybackPositionEvent>(scope,
    playbackPositionFlow,
    dispatcher,
    mediaController,
    PlaybackPositionEvent.DEFAULT) {

    companion object {
        fun initialise(viewModel: ViewModel,
                       metadataFlow: PlaybackPositionFlow,
                       @MainDispatcher dispatcher: CoroutineDispatcher,
                       mediaControllerAsync : ListenableFuture<MediaController>) : PlaybackPosition {
            return PlaybackPosition(viewModel.viewModelScope, metadataFlow, dispatcher, mediaControllerAsync)
        }
    }

    override suspend fun initialValue(): PlaybackPositionEvent {
        val mediaController = mediaControllerAsync.await()
        val isPlaying = mediaController.isPlaying
        val currentPosition = mediaController.currentPosition
        val currentTime = TimerUtils.getSystemTime()
        return PlaybackPositionEvent(isPlaying, currentPosition, currentTime)
    }
}