package com.github.goldy1992.mp3player.service.player

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Handler
import androidx.annotation.VisibleForTesting
import androidx.media3.exoplayer.ExoPlayer

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
            context.registerReceiver(this, audioNoisyIntentFilter, Manifest.permission.MODIFY_AUDIO_SETTINGS,null)
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