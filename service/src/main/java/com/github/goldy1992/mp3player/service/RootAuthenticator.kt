package com.github.goldy1992.mp3player.service

import androidx.annotation.VisibleForTesting
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_NONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME_KEY
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import javax.inject.Inject
import androidx.annotation.OptIn as AndroidXOptIn

@AndroidXOptIn(UnstableApi::class)
class RootAuthenticator @Inject constructor(ids: MediaItemTypeIds) {
    private val acceptedMediaId: String = ids.getId(MediaItemType.ROOT)

    private val rootItem = MediaItemBuilder(acceptedMediaId)
        .setFolderType(FOLDER_TYPE_NONE)
        .setIsPlayable(false)
        .setMediaItemType(MediaItemType.ROOT)
        .build()

    private val rejectedRootItem = MediaItemBuilder(REJECTED_MEDIA_ROOT_ID)
        .setFolderType(FOLDER_TYPE_NONE)
        .setIsPlayable(false)
        .setMediaItemType(MediaItemType.ROOT)
        .build()

    fun authenticate(params : MediaLibraryService.LibraryParams): LibraryResult<MediaItem> {
        val clientPackageName : String = params.extras.getString(PACKAGE_NAME_KEY) ?: ""
        // (Optional) Control the level of access for the specified package name.
// You'll need to write your own logic to do this.
        return if (allowBrowsing(clientPackageName)) { // Returns a root ID that clients can use with onLoadChildren() to retrieve
// the content hierarchy.
            LibraryResult.ofItem(
                rootItem,
                params)
        } else { // Clients can connect, but this BrowserRoot is an empty hierachy
// so onLoadChildren returns nothing. This disables the ability to browse for content.
            LibraryResult.ofItem(rejectedRootItem, params)
        }
    }

    fun getRootItem() : MediaItem {
        return rootItem
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