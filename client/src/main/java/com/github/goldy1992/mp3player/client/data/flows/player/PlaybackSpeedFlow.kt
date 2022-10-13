package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.concurrent.futures.await
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
class PlaybackSpeedFlow
@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            scope : CoroutineScope,
            @MainDispatcher mainDispatcher: CoroutineDispatcher,
            playbackParametersFlow: PlaybackParametersFlow
) : LogTagger, PlayerFlow<Float>(mediaControllerFuture, scope, mainDispatcher, 1.0f) {

    private val playbackSpeedFlow : Flow<Float> = playbackParametersFlow.state
        .map {
            it.speed
        }.shareIn(
            scope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    override fun logTag(): String {
        return "PlaybackSpeedFlow"
    }

    override fun flow(): Flow<Float> {
        return playbackSpeedFlow
    }


    override suspend fun getInitialValue(): Float {
        return mediaControllerFuture.await().playbackParameters.speed
    }

    private suspend fun getQueue(player : Player) : List<MediaItem> {

        val playlist = mutableListOf<MediaItem>()
        val job = scope.launch(mainDispatcher) {
            val count : Int = player.mediaItemCount ?: 0
            for (i in 0 until count) {
                playlist.add(i, player.getMediaItemAt(i))
            }
        }
        job.join()
        return playlist.toList()
    }

    init {
        initialise()
    }

}