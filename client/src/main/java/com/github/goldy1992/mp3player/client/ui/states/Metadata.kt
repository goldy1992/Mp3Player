package com.github.goldy1992.mp3player.client.ui.states

import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@ViewModelScoped
class Metadata

private constructor(scope: CoroutineScope,
                    metadataFlow: MetadataFlow,
                    @MainDispatcher dispatcher: CoroutineDispatcher,
                    mediaControllerAsync : ListenableFuture<MediaController>)
    : PlayerFlowState<MediaMetadata>(scope,
    metadataFlow,
    dispatcher,
    mediaControllerAsync,
    MediaMetadata.EMPTY) {

    companion object {
        fun initialise(viewModel: ViewModel,
                       metadataFlow: MetadataFlow,
                       @MainDispatcher dispatcher: CoroutineDispatcher,
                       mediaControllerAsync : ListenableFuture<MediaController>) : Metadata {
            return Metadata(viewModel.viewModelScope, metadataFlow, dispatcher, mediaControllerAsync)
        }
    }

    override suspend fun initialValue(): MediaMetadata {
        return mediaControllerAsync.await().mediaMetadata
    }
}