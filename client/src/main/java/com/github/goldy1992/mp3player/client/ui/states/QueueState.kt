package com.github.goldy1992.mp3player.client.ui.states

import com.github.goldy1992.mp3player.client.data.Song

data class QueueState(
    val items : List<Song>,
    val currentIndex : Int)
{
    companion object {
        val EMPTY = QueueState(emptyList(), 0)
    }
}
