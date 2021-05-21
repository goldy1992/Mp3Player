package com.github.goldy1992.mp3player.service.player

import android.os.Bundle
import android.util.Log
import com.github.goldy1992.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.CustomActionProvider

abstract class SpeedChangeActionProviderBase : CustomActionProvider {
    override fun onCustomAction(player: Player, controlDispatcher: ControlDispatcher, action: String, extras: Bundle?) {
        Log.i(LOG_TAG, "hit speed change")
        val currentSpeed = player.playbackParameters.speed
        var newSpeed = currentSpeed
        when (action) {
            INCREASE_PLAYBACK_SPEED -> newSpeed += DEFAULT_PLAYBACK_SPEED_CHANGE
            DECREASE_PLAYBACK_SPEED -> newSpeed -= DEFAULT_PLAYBACK_SPEED_CHANGE
            else -> {
            }
        }
        changeSpeed(newSpeed, player)
    }

    /**
     *
     * @param speed the speed
     * @return true if it's a valid speed
     */
    private fun validSpeed(speed: Float): Boolean {
        return speed in MINIMUM_PLAYBACK_SPEED..MAXIMUM_PLAYBACK_SPEED
    }

    private fun changeSpeed(newSpeed: Float, player: Player) {
        if (validSpeed(newSpeed)) {
            val newPlaybackParameters = PlaybackParameters(newSpeed)
            player.setPlaybackParameters(newPlaybackParameters)
        }
    }

    companion object {
        private const val MINIMUM_PLAYBACK_SPEED = 0.25f
        private const val MAXIMUM_PLAYBACK_SPEED = 2f
        private const val DEFAULT_PLAYBACK_SPEED_CHANGE = 0.05f
        private const val LOG_TAG = "ACTN_PRVDR"
    }
}