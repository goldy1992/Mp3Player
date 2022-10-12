package com.github.goldy1992.mp3player.client.data.eventholders

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService

data class OnChildrenChangedEventHolder(
    val browser: MediaBrowser,
    val parentId: String,
    val itemCount: Int,
    val params: MediaLibraryService.LibraryParams?)
