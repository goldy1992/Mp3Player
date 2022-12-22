package com.github.goldy1992.mp3player.client.ui.flows.player

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ActivityRetainedScoped
class IsPlayingFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<Player>) : LogTagger, PlayerFlow<Boolean>(mediaControllerFuture) {

    private val isPlayingFlow : Flow<Boolean> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i(logTag(), "onIsPlayingChanged: $isPlaying")
                trySend(isPlaying)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            controller.removeListener(messageListener)
        }
    }

    override fun logTag(): String {
        return "IsPlayingFlow"
    }

    override fun flow(): Flow<Boolean> {
        return isPlayingFlow
    }


}