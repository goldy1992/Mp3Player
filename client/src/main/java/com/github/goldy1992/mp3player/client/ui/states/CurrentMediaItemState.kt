package com.github.goldy1992.mp3player.client.ui.states

import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ViewModelScoped
class CurrentMediaItemState
    private constructor(private val scope: CoroutineScope,
                        private val metadataFlow: MetadataFlow,
                        @MainDispatcher private val dispatcher: CoroutineDispatcher,
                        private val mediaControllerAsync : ListenableFuture<MediaController>
    ) {

    private val backingState = MutableStateFlow(MediaItem.EMPTY)
    val state : StateFlow<MediaItem> = backingState

    init {
        scope.launch(dispatcher) {
            backingState.value = getCurrentMediaItem()
        }
        scope.launch {
            metadataFlow.flow().collect {
                backingState.value = getCurrentMediaItem()
            }
        }
    }

    private suspend fun getCurrentMediaItem() : MediaItem {
        return mediaControllerAsync.await().currentMediaItem ?: MediaItem.EMPTY
    }

    companion object {
        fun initialise(
            viewModel: ViewModel,
            metadataFlow: MetadataFlow,
            @MainDispatcher dispatcher: CoroutineDispatcher,
            mediaControllerAsync: ListenableFuture<MediaController>
        ): CurrentMediaItemState {
            return CurrentMediaItemState(
                viewModel.viewModelScope,
                metadataFlow,
                dispatcher,
                mediaControllerAsync
            )
        }
    }
}