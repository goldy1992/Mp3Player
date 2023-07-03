package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.data.Song

data class QueueChangedEvent(
    val items : List<Song>,
    val currentIndex : Int)
{
    companion object {
        val EMPTY = QueueChangedEvent(emptyList(), 0)
    }
}
