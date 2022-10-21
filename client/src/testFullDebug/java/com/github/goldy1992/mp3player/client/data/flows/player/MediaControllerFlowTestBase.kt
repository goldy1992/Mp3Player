package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.CoroutineTestBase
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import org.junit.Before
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.stubbing.Answer

abstract class MediaControllerFlowTestBase : CoroutineTestBase() {

    protected val mediaController = mock<MediaController>()
    protected val mediaControllerListenableFuture : ListenableFuture<MediaController> = Futures.immediateFuture(mediaController)
    var listener : Player.Listener? = null
    @Before
    open fun setup() {
        val answer = Answer {
            val argumentListener : Player.Listener = it.getArgument(0, Player.Listener::class.java) as Player.Listener
            listener = argumentListener
            argumentListener
        }
        whenever(mediaController.addListener(any())).thenAnswer(answer)
    }
}