package com.github.goldy1992.mp3player.service.player

import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.nhaarman.mockitokotlin2.mock

open class SpeedProviderTestBase {

    var exoPlayer: ExoPlayer = mock<ExoPlayer>()
    lateinit var handler: Handler
    var controlDispatcher: ControlDispatcher = mock<ControlDispatcher>()

    open fun setup() {
        handler = Handler(Looper.getMainLooper())
    }
}