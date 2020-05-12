package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import javax.inject.Inject

class MyControlDispatcher @Inject constructor(private val audioBecomingNoisyBroadcastReceiver: AudioBecomingNoisyBroadcastReceiver,
                                              private val playerNotificationManager: MyPlayerNotificationManager) : DefaultControlDispatcher() {
    override fun dispatchSetPlayWhenReady(player: Player, playWhenReady: Boolean): Boolean {
        if (playWhenReady) {
            audioBecomingNoisyBroadcastReceiver.register()
            if (!playerNotificationManager.isActive) {
                playerNotificationManager.activate()
            }
        } else {
            audioBecomingNoisyBroadcastReceiver.unregister()
        }
        return super.dispatchSetPlayWhenReady(player, playWhenReady)
    }

}