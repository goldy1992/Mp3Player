package com.github.goldy1992.mp3player.client.ui.states

import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.flows.player.PlayerFlow
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


abstract class PlayerFlowState<T>
    protected constructor(scope: CoroutineScope,
                          flow: PlayerFlow<T>,
                          @MainDispatcher protected val dispatcher: CoroutineDispatcher,
                          protected val mediaControllerAsync : ListenableFuture<MediaController>,
                          initialValue : T
    ) : ViewModelFlowState<T>(scope, flow.flow(), initialValue) {


    init {
        scope.launch(dispatcher) {
            backingState.value = initialValue()
        }
    }





    }