package com.github.goldy1992.mp3player.service

import androidx.annotation.VisibleForTesting
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_NONE
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME_KEY
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import javax.inject.Inject

class RootAuthenticator @Inject constructor(ids: MediaItemTypeIds) {
    private val acceptedMediaId: String = ids.getId(MediaItemType.ROOT)

    @Suppress("UNUSED_PARAMETER")
    fun authenticate(params : MediaLibraryService.LibraryParams): LibraryResult<MediaItem> {
        val clientPackageName : String = params.extras.getString(PACKAGE_NAME_KEY) ?: ""
        // (Optional) Control the level of access for the specified package name.
// You'll need to write your own logic to do this.
        return if (allowBrowsing(clientPackageName)) { // Returns a root ID that clients can use with onLoadChildren() to retrieve
// the content hierarchy.
            LibraryResult.ofItem(
                MediaItem.Builder()
                    .setMediaId(acceptedMediaId)
                    .setMediaMetadata(MediaMetadata.Builder()
                        .setFolderType(FOLDER_TYPE_NONE)
                        .setIsPlayable(false)
                        .build())
                    .build(), params)
        } else { // Clients can connect, but this BrowserRoot is an empty hierachy
// so onLoadChildren returns nothing. This disables the ability to browse for content.
            LibraryResult.ofItem(MediaItem.Builder().setMediaId(REJECTED_MEDIA_ROOT_ID).build(), params)
        }
    }

    fun rejectRootSubscription(id: String): Boolean {
        return REJECTED_MEDIA_ROOT_ID == id
    }

    private fun allowBrowsing(clientPackageName: String): Boolean {
        return clientPackageName.contains(PACKAGE_NAME)
    }

    companion object {
        @JvmField
        @VisibleForTesting
        val REJECTED_MEDIA_ROOT_ID = "empty_root_id"
    }

}