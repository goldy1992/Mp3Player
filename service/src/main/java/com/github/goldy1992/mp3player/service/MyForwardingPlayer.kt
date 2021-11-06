package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ForwardingPlayer
import javax.inject.Inject

class MyForwardingPlayer

    @Inject
    constructor(
        player: ExoPlayer,
        private val audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver,
        private val playerNotificationManager: MyPlayerNotificationManager) : ForwardingPlayer(player) {

    override fun setPlayWhenReady(playWhenReady: Boolean) {

        if (playWhenReady) {
            audioBecomingNoisyBroadcastReceiver.register()
            if (!playerNotificationManager.isActive) {
                playerNotificationManager.activate()
            }
        } else {
            audioBecomingNoisyBroadcastReceiver.unregister()
        }

        super.setPlayWhenReady(playWhenReady)
    }
}