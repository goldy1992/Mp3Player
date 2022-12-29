package com.github.goldy1992.mp3player.client.ui.states

import androidx.media3.common.MediaItem

data class QueueState(
    val items : List<MediaItem>,
    val currentIndex : Int)
{
    companion object {
        val EMPTY = QueueState(emptyList(), 0)
    }
}
