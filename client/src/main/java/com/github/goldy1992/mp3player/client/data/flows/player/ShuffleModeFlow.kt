package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.concurrent.futures.await
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped
class ShuffleModeFlow
    @Inject
    constructor(mediaControllerFuture: ListenableFuture<MediaController>,
                scope : CoroutineScope,
                @MainDispatcher mainDispatcher: CoroutineDispatcher
    ) : LogTagger, PlayerFlow<Boolean>(mediaControllerFuture, scope, mainDispatcher, false) {

        private val shuffleModeCallbackFlow : Flow<Boolean> = callbackFlow {
            val controller = mediaControllerFuture.await()
            val messageListener = object : Player.Listener {
                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    trySend(shuffleModeEnabled)
                }
            }
            controller.addListener(messageListener)
            awaitClose { controller.removeListener(messageListener) }
        }.shareIn(
            scope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )
        override fun flow(): Flow<Boolean> {
            return shuffleModeCallbackFlow
        }

        override fun logTag(): String {
            return "ShuffleModeFlow"
        }



        override suspend fun getInitialValue(): Boolean {
            return mediaControllerFuture.await().shuffleModeEnabled
        }

        init {
            initialise()
        }

    }