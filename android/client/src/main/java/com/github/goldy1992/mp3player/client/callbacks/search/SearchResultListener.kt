package com.github.goldy1992.mp3player.client.callbacks.search

import android.support.v4.media.MediaBrowserCompat

interface SearchResultListener {
    fun onSearchResult(searchResults: List<MediaBrowserCompat.MediaItem>?)
}