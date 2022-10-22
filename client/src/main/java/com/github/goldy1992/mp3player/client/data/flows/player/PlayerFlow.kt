package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class PlayerFlow<T>

constructor(
    protected val mediaControllerFuture: ListenableFuture<MediaController>,
    protected val scope: CoroutineScope) {

    abstract fun flow() : Flow<T>

}