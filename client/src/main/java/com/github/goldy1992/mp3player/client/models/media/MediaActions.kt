package com.github.goldy1992.mp3player.client.models.media

import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.ui.viewmodel.MediaViewModel

data class MediaActions(
    val play: () -> Unit = {},
    val pause: () -> Unit = {},
    val skipToNext: () -> Unit = {},
    val skipToPrevious: () -> Unit = {},
    val setRepeatMode: (RepeatMode) -> Unit = {_->},
    val setShuffleEnabled: (Boolean) -> Unit = {_->},
    val setPlaybackSpeed: (Float) -> Unit = {_->},
    val setPlaybackPosition: (Float) -> Unit = {_->},
    val playSong : (Song) -> Unit = {_->},
    val isAlbumPlaying : (Boolean, String, Album) -> Boolean = {_,_,_-> false},
    val onPlayAlbum : (String, Album) -> Unit = {_,_->},
    val onAlbumSongSelected: (Int, Album) -> Unit = {_,_,->}
) {
    companion object {
        val DEFAULT = MediaActions()

        fun create(mediaViewModel: MediaViewModel) : MediaActions {
            return MediaActions(
                play = { mediaViewModel.play() },
                pause = { mediaViewModel.pause() },
                skipToNext = { mediaViewModel.skipToNext() },
                skipToPrevious = { mediaViewModel.skipToPrevious() }
            )
        }
    }
}