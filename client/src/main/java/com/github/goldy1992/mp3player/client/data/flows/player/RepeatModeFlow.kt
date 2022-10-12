package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.concurrent.futures.await
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.RepeatMode
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped
class RepeatModeFlow

    @Inject
    constructor(mediaControllerFuture: ListenableFuture<MediaController>,
                scope : CoroutineScope
) : LogTagger, PlayerFlow<@RepeatMode Int>(mediaControllerFuture, REPEAT_MODE_OFF, scope) {

    private val repeatModeCallbackFlow : Flow<@RepeatMode Int> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onRepeatModeChanged(@RepeatMode repeatMode: Int) {
                trySend(repeatMode)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
    override fun flow(): Flow<@RepeatMode Int> {
        return repeatModeCallbackFlow
    }

    override fun logTag(): String {
        return "RepeatModeFlow"
    }

    init {
        initialise()
    }

}