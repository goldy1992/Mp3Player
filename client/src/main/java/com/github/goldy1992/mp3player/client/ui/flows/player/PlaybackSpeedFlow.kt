package com.github.goldy1992.mp3player.client.ui.flows.player

import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class PlaybackSpeedFlow
@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            playbackParametersFlow: PlaybackParametersFlow
) : LogTagger, PlayerFlow<Float>(mediaControllerFuture) {

    private val playbackSpeedFlow : Flow<Float> = playbackParametersFlow.flow()
        .map {
            it.speed
        }

    override fun logTag(): String {
        return "PlaybackSpeedFlow"
    }

    override fun flow(): Flow<Float> {
        return playbackSpeedFlow
    }
}