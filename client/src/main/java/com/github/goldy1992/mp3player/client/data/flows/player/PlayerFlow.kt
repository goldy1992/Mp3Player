package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class PlayerFlow<T>

constructor(
    protected val mediaControllerFuture: ListenableFuture<MediaController>,
    protected val scope: CoroutineScope,
    @MainDispatcher protected val mainDispatcher: CoroutineDispatcher,
    initialValue : T,){

    protected abstract fun flow() : Flow<T>

    private val backingState = MutableStateFlow(initialValue)
    val state : StateFlow<T> = backingState

    protected fun initialise() {
        scope.launch {
            flow().collect {
                backingState.value = it
            }
        }
        scope.launch(mainDispatcher) { backingState.value = getInitialValue() }
    }

    abstract suspend fun getInitialValue() : T
}