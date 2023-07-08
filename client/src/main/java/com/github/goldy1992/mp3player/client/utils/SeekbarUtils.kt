package com.github.goldy1992.mp3player.client.utils

import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.TimerUtils

object SeekbarUtils {

    fun calculateCurrentPosition(playbackPositionEvent: PlaybackPositionEvent) : Long {
        return if (playbackPositionEvent.isPlaying) {
            playbackPositionEvent.currentPosition + (TimerUtils.getSystemTime() - playbackPositionEvent.systemTime)
        } else {
            playbackPositionEvent.currentPosition
        }
    }

    fun calculateAnimationTime(
        currentPosition: Float,
        duration: Float,
        playbackSpeed: Float
    ) : Int {
        val remainingPlaybackTime = duration - currentPosition
        return  (remainingPlaybackTime / playbackSpeed).toInt()
    }
}