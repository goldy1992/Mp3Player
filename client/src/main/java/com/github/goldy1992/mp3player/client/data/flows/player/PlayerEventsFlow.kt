package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.concurrent.futures.await
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.data.eventholders.PlayerEventHolder
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerEventsFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            scope : CoroutineScope,
            @MainDispatcher mainDispatcher: CoroutineDispatcher
) : LogTagger, PlayerFlow<PlayerEventHolder>(mediaControllerFuture, scope, mainDispatcher, PlayerEventHolder.EMPTY) {

    private val eventsFlow : Flow<PlayerEventHolder> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                trySend(PlayerEventHolder(player, events))
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    override fun logTag(): String {
        return "PlayerEventsFlow"
    }

    override fun flow(): Flow<PlayerEventHolder> {
        return eventsFlow
    }

    init {
        initialise()
        scope.launch {

        }
    }

    override suspend fun getInitialValue(): PlayerEventHolder {
        return PlayerEventHolder.EMPTY
    }
}