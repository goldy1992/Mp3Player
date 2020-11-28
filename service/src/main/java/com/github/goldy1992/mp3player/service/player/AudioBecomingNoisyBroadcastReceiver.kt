package com.github.goldy1992.mp3player.service.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.annotation.VisibleForTesting
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AudioBecomingNoisyBroadcastReceiver
/**
 * Constructor
 */ @Inject constructor(@ApplicationContext private val context: Context, private val player: ExoPlayer) : BroadcastReceiver() {
    @get:VisibleForTesting
    var isRegistered = false
        private set

    @Synchronized
    override fun onReceive(context: Context, intent: Intent) {
        val intentIsAudioBecomingNoisy = AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action
        if (intentIsAudioBecomingNoisy) {
            player.playWhenReady = false // pause the player
        }
    }

    fun register() {
        if (!isRegistered) {
            val audioNoisyIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
            context.registerReceiver(this, audioNoisyIntentFilter)
            isRegistered = true
        }
    }

    fun unregister() {
        if (isRegistered) {
            context.unregisterReceiver(this)
            isRegistered = false
        }
    }

}