package com.github.goldy1992.mp3player.client.ui.flows.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.*
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class QueueFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            playerEventsFlow: PlayerEventsFlow,
) : LogTagger, PlayerFlow<QueueState>(mediaControllerFuture) {

    private val events : @Event IntArray = intArrayOf(EVENT_MEDIA_ITEM_TRANSITION, EVENT_TIMELINE_CHANGED)

    private val queueFlow : Flow<QueueState> = playerEventsFlow.flow()
        .filter { it.events.containsAny( *events ) }
        .map {
           getQueue(it.player!!)
        }

    override fun logTag(): String {
        return "PlayerEventsFlow"
    }

    override fun flow(): Flow<QueueState> {
        return queueFlow
    }

    private fun getQueue(player : Player) : QueueState {
        val playlist = mutableListOf<MediaItem>()
            val count : Int = player.mediaItemCount
            for (i in 0 until count) {
                playlist.add(i, player.getMediaItemAt(i))
            }

        return QueueState(playlist.toList(), player.currentMediaItemIndex)
    }
}