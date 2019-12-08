package com.github.goldy1992.mp3player.service.player

import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackParameters
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock

open class SpeedProviderTestBase {
    @Mock
    var exoPlayer: ExoPlayer? = null
    var handler: Handler? = null
    @Captor
    var captor: ArgumentCaptor<PlaybackParameters>? = null
    @Mock
    var controlDispatcher: ControlDispatcher? = null

    open fun setup() {
        handler = Handler(Looper.getMainLooper())
    }
}