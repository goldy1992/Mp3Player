package com.github.goldy1992.mp3player.service.player

import android.os.Bundle
import android.util.Log
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class ChangeSpeedProvider

@Inject
constructor() : LogTagger {

    fun changeSpeed(player: Player, args : Bundle) {
        Log.i(logTag(), "hit speed change")
        val newSpeed: Float = args.getFloat(Constants.CHANGE_PLAYBACK_SPEED)
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
            player.playbackParameters = newPlaybackParameters
        }
    }

    companion object {
        private const val MINIMUM_PLAYBACK_SPEED = 0.25f
        private const val MAXIMUM_PLAYBACK_SPEED = 2f
        private const val LOG_TAG = "ACTN_PRVDR"
    }

    override fun logTag(): String {
        return LOG_TAG
    }
}