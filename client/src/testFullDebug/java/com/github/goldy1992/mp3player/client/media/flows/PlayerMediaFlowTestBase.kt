package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.media.PlayerTestImpl
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

abstract class PlayerMediaFlowTestBase<T> : MediaFlowTestBase<T>() {

    protected val testPlayer = PlayerTestImpl()
    protected val controllerFuture : ListenableFuture<Player> = Futures.immediateFuture(testPlayer)

}