package com.github.goldy1992.mp3player.service.player

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.service.R
import com.google.android.exoplayer2.Player
import javax.inject.Inject

class DecreaseSpeedProvider @Inject constructor() : SpeedChangeActionProviderBase() {
    override fun getCustomAction(player: Player): PlaybackStateCompat.CustomAction {
        return PlaybackStateCompat.CustomAction.Builder(DECREASE_PLAYBACK_SPEED, DECREASE_PLAYBACK_SPEED, R.drawable.border).build()
    }
}