package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped
class PlaybackSpeedFlow
@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            scope : CoroutineScope,
            playbackParametersFlow: PlaybackParametersFlow
) : LogTagger, PlayerFlow<Float>(mediaControllerFuture, scope) {

    private val playbackSpeedFlow : Flow<Float> = playbackParametersFlow.flow()
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
}