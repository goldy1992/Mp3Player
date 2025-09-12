package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IsPlayingFlow

internal constructor(
    @ActivityCoroutineScope scope : CoroutineScope,
    private val controllerFuture : ListenableFuture<Player>,
    @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
    onCollect : (Boolean) -> Unit
            )  : FlowBase<Boolean>(scope, onCollect)
{

    companion object {
        const val LOG_TAG = "IsPlayingFlow"
        fun create(
            @ActivityCoroutineScope scope : CoroutineScope,
            controllerFuture : ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect : (Boolean) -> Unit) : IsPlayingFlow {
            val isPlayingFlow = IsPlayingFlow(scope, controllerFuture, mainDispatcher, onCollect)
            isPlayingFlow.initFlow(isPlayingFlow.getFlow())
            return isPlayingFlow
        }
    }


    override fun getFlow(): Flow<Boolean> = callbackFlow {
        Log.v(LOG_TAG, "isPlayingFlow invoked, awaiting MediaController")
        val controller = controllerFuture.await()
        Log.v(LOG_TAG, "isPlayingFlow got MediaController")
        var isPlaying: Boolean
        withContext(mainDispatcher) {
            isPlaying = controller.isPlaying
        }
        trySend(isPlaying)
        val messageListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.v(LOG_TAG, "onIsPlayingChanged() invoked, isPlaying: $isPlaying")
                trySend(isPlaying)
            }

        }
        controller.addListener(messageListener)
        awaitClose {
            Log.d(LOG_TAG, "isPlayingCallbackFlow() awaitClose invoked")
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }


}