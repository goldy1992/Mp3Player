package com.github.goldy1992.mp3player.service

import android.support.v4.media.MediaBrowserCompat.MediaItem
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject
import javax.inject.Named

@ServiceScoped
class PlaylistManager
    @Inject
    constructor(
            @param:Named("starting_playlist") val playlist: MutableList<MediaItem>?) {

    private var queueIndex = EMPTY_PLAYLIST_INDEX

    @Suppress("UNCHECKED_CAST")
    fun createNewPlaylist(newList: List<MediaItem?>?): Boolean {
        playlist!!.clear()
        val result = playlist.addAll(newList as List<MediaItem>)
        queueIndex = if (playlist.isEmpty()) EMPTY_PLAYLIST_INDEX else START_OF_PLAYLIST
        return result
    }

    @Synchronized
    fun getItemAtIndex(index: Int): MediaItem? {
        return if (validQueueIndex(index)) {
            playlist!![index]
        } else null
    }

    @get:Synchronized
    val currentItem: MediaItem?
        get() = if (validQueueIndex(queueIndex)) {
            playlist!![queueIndex]
        } else null

    @Synchronized
    private fun validQueueIndex(newQueueIndex: Int): Boolean {
        return newQueueIndex < playlist!!.size && newQueueIndex >= 0
    }

    companion object {
        private const val START_OF_PLAYLIST = 0
        private const val EMPTY_PLAYLIST_INDEX = -1
    }

    init {
        queueIndex = START_OF_PLAYLIST
    }
}