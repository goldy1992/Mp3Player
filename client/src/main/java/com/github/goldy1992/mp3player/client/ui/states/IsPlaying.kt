package com.github.goldy1992.mp3player.client.ui.states

import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@ViewModelScoped
class IsPlaying

    private constructor(scope: CoroutineScope,
                        isPlayingFlow: IsPlayingFlow,
                        @MainDispatcher dispatcher: CoroutineDispatcher,
                        mediaControllerAsync : ListenableFuture<MediaController>)
    : PlayerFlowState<Boolean>(scope,
                                isPlayingFlow,
                                dispatcher,
                                mediaControllerAsync,
                                false) {

    companion object {
        fun initialise(viewModel: ViewModel,
                       playerFlow: IsPlayingFlow,
                       @MainDispatcher dispatcher: CoroutineDispatcher,
                       mediaControllerAsync : ListenableFuture<MediaController>) : IsPlaying {
            return IsPlaying(viewModel.viewModelScope, playerFlow, dispatcher, mediaControllerAsync)
        }
    }

    override suspend fun initialValue(): Boolean {
        return mediaControllerAsync.await().isPlaying
    }
}