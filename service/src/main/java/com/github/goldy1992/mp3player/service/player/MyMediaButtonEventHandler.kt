package com.github.goldy1992.mp3player.service.player

import android.content.Intent
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.MediaButtonEventHandler
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MyMediaButtonEventHandler

    @Inject
    constructor(): MediaButtonEventHandler {
    override fun onMediaButtonEvent(player: Player, mediaButtonEvent: Intent): Boolean {
        return false
    }
}