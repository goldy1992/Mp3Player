package com.github.goldy1992.mp3player.client.data.repositories.media.eventholders

import androidx.media3.common.MediaItem

data class OnQueueChangedEventHolder(
    val items : List<MediaItem>,
    val currentIndex : Int)
{
    companion object {
        val EMPTY = OnQueueChangedEventHolder(emptyList(), 0)
    }
}
