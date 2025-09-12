package com.github.goldy1992.mp3player.client.utils

import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.utils.TimerUtils

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

    fun validSong(song: Song) : Boolean {
        return song != Song.DEFAULT
    }

    fun validPlaybackPosition(song: Song, playbackPositionEvent: PlaybackPositionEvent) : Boolean {
        val currentPosition = playbackPositionEvent.currentPosition
        return currentPosition in 0L..song.duration
    }
}