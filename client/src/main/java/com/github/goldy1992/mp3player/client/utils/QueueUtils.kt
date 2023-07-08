package com.github.goldy1992.mp3player.client.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder

object QueueUtils {
    fun getQueue(player : Player) : OnQueueChangedEventHolder {
        val playlist = mutableListOf<MediaItem>()
        val count : Int = player.mediaItemCount
        for (i in 0 until count) {
            playlist.add(i,player.getMediaItemAt(i))
        }

        return OnQueueChangedEventHolder(playlist.toList(), player.currentMediaItemIndex)
    }
}