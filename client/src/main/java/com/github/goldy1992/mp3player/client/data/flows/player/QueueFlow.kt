package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class QueueFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            scope : CoroutineScope,
            playerEventsFlow: PlayerEventsFlow,
            @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : LogTagger, PlayerFlow<List<MediaItem>>(mediaControllerFuture, scope) {

    private val queueFlow : Flow<List<MediaItem>> = playerEventsFlow.flow()
        .filter { it.events.contains(Player.EVENT_TIMELINE_CHANGED) }
        .map {
           getQueue(it.player!!)
        }.shareIn(
            scope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    override fun logTag(): String {
        return "PlayerEventsFlow"
    }

    override fun flow(): Flow<List<MediaItem>> {
        return queueFlow
    }

    suspend fun getQueue(player : Player) : List<MediaItem> {
        val playlist = mutableListOf<MediaItem>()
        val job = scope.launch(mainDispatcher) {
            val count : Int = player.mediaItemCount
            for (i in 0 until count) {
                playlist.add(i, player.getMediaItemAt(i))
            }
        }
        job.join()
        return playlist.toList()
    }
}