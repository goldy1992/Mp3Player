package com.github.goldy1992.mp3player.client.ui.flows.player

import androidx.concurrent.futures.await
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlayerEventHolder
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class PlayerEventsFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>
) : LogTagger, PlayerFlow<PlayerEventHolder>(mediaControllerFuture) {

    private val eventsFlow : Flow<PlayerEventHolder> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                trySend(PlayerEventHolder(player, events))
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }

    override fun logTag(): String {
        return "PlayerEventsFlow"
    }

    override fun flow(): Flow<PlayerEventHolder> {
        return eventsFlow
    }
}