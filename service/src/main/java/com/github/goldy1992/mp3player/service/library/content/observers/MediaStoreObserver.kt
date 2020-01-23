package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds

abstract class MediaStoreObserver(handler: Handler?,
                                  private val contentResolver: ContentResolver,
                                  val mediaItemTypeIds: MediaItemTypeIds) : ContentObserver(handler), LogTagger {
    var mediaPlaybackService: MediaPlaybackService? = null
    fun init(mediaPlaybackService: MediaPlaybackService?) {
        this.mediaPlaybackService = mediaPlaybackService
    }

    fun register() {
        contentResolver.registerContentObserver(uri, true, this)
    }

    fun unregister() {
        contentResolver.unregisterContentObserver(this)
    }

    fun startsWithUri(uri: Uri?): Boolean {
        return uri != null && uri.toString().startsWith(uriString)
    }

    abstract val uri: Uri
    val uriString: String
        get() = uri.toString()

}