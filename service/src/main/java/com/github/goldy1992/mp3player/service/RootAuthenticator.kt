package com.github.goldy1992.mp3player.service

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.media.MediaBrowserServiceCompat
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import javax.inject.Inject

class RootAuthenticator @Inject constructor(ids: MediaItemTypeIds) {
    private val acceptedMediaId: String
    fun authenticate(clientPackageName: String, clientUid: Int,
                     rootHints: Bundle?): MediaBrowserServiceCompat.BrowserRoot {
        val extras = Bundle()
        // (Optional) Control the level of access for the specified package name.
// You'll need to write your own logic to do this.
        return if (allowBrowsing(clientPackageName, clientUid)) { // Returns a root ID that clients can use with onLoadChildren() to retrieve
// the content hierarchy.
            MediaBrowserServiceCompat.BrowserRoot(acceptedMediaId, extras)
        } else { // Clients can connect, but this BrowserRoot is an empty hierachy
// so onLoadChildren returns nothing. This disables the ability to browse for content.
            MediaBrowserServiceCompat.BrowserRoot(REJECTED_MEDIA_ROOT_ID, extras)
        }
    }

    fun rejectRootSubscription(id: String): Boolean {
        return REJECTED_MEDIA_ROOT_ID == id
    }

    private fun allowBrowsing(clientPackageName: String, clientUid: Int): Boolean {
        return clientPackageName.contains(PACKAGE_NAME)
    }

    companion object {
        @JvmField
        @VisibleForTesting
        val REJECTED_MEDIA_ROOT_ID = "empty_root_id"
    }

    init {
        acceptedMediaId = ids.getId(MediaItemType.ROOT)!!
    }
}