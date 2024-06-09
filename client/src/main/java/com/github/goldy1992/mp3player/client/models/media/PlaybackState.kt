package com.github.goldy1992.mp3player.client.models.media

import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.RepeatMode

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentSong: Song = Song.DEFAULT,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleEnabled: Boolean = false,
    val playbackSpeed: Float = 1f,
    val playbackPosition: PlaybackPositionEvent = PlaybackPositionEvent.DEFAULT,
    val actions: MediaActions = MediaActions.DEFAULT
) {
    companion object {
        val DEFAULT = PlaybackState()
    }
}