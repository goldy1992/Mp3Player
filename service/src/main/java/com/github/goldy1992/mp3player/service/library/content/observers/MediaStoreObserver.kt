package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds

abstract class MediaStoreObserver(private val contentResolver: ContentResolver,
                                  val mediaItemTypeIds: MediaItemTypeIds) : ContentObserver(Handler(Looper.myLooper() ?: Looper.getMainLooper())), LogTagger {
    var mediaSession: MediaLibrarySession? = null
    fun init(mediaLibrarySession: MediaLibrarySession) {
        this.mediaSession = mediaLibrarySession
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