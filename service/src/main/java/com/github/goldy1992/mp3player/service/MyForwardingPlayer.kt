package com.github.goldy1992.mp3player.service

import androidx.media3.common.ForwardingPlayer
import androidx.media3.exoplayer.ExoPlayer
import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
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