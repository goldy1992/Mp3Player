package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Song

data class PlaybackState(
    val isPlayingProvider : () -> Boolean = {false},
    val onClickPlay: () -> Unit = {},
    val onClickPause: () -> Unit = {},
    val onClickSkipNext: () -> Unit = {},
    val onClickSkipPrevious: () -> Unit = {},
    val currentSongProvider : () -> Song = { Song.DEFAULT },
    val playbackSpeedProvider : () ->  Float = {1.0f},
    val playbackPositionProvider: () -> PlaybackPositionEvent = { PlaybackPositionEvent.DEFAULT},

    ) {
    companion object {
        val DEFAULT = PlaybackState()
    }
}