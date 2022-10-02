package com.github.goldy1992.mp3player.service

import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager
import javax.inject.Inject

class MyForwardingPlayer

    @Inject
    constructor(
        player: ExoPlayer,
        private val audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver) : ForwardingPlayer(player) {

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        if (playWhenReady) {
            audioBecomingNoisyBroadcastReceiver.register()
        } else {
            audioBecomingNoisyBroadcastReceiver.unregister()
        }
        super.setPlayWhenReady(playWhenReady)
    }
}