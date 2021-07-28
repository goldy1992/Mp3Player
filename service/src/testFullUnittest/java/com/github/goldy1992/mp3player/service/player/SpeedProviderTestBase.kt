package com.github.goldy1992.mp3player.service.player

import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import org.mockito.kotlin.mock

open class SpeedProviderTestBase {

    var exoPlayer: ExoPlayer = mock<ExoPlayer>()
    var controlDispatcher: ControlDispatcher = mock<ControlDispatcher>()

}