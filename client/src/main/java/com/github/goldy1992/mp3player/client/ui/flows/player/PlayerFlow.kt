package com.github.goldy1992.mp3player.client.ui.flows.player

import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.Flow

abstract class PlayerFlow<T>

constructor(
    protected val mediaControllerFuture: ListenableFuture<Player>) {

    abstract fun flow() : Flow<T>

}