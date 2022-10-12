package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.Constants.EMPTY_MEDIA_ITEM_ID
import java.io.File

object MediaItemUtils {
    private fun hasExtras(item: MediaItem?): Boolean {
        return item != null && item.mediaMetadata.extras != null
    }

    private fun hasTitle(item: MediaItem?): Boolean {
        return item != null && item.mediaMetadata.title != null
    }

    private fun hasDescription(item: MediaItem?): Boolean {
        return item != null && item.mediaMetadata.description != null
    }

    @JvmStatic
    fun getExtras(item: MediaItem): Bundle? {
        return if (!hasExtras(item)) {
            null
        } else item.mediaMetadata.extras
    }

    fun getExtra(key: String?, item: MediaItem?): Any? {
        if (item == null) {
            return null
        }
        val extras = item.mediaMetadata.extras
        return extras?.get(key)
    }

    @JvmStatic
    fun getMediaId(item: MediaItem?): String? {
        return item?.mediaId
    }

    @JvmStatic
    fun getTitle(mediaItem: MediaItem): String {
        return if (hasTitle(mediaItem)) {
            mediaItem.mediaMetadata.title.toString()
        } else Constants.UNKNOWN
    }

    @JvmStatic
    fun getDescription(item: MediaItem): String? {
        return if (hasDescription(item)) {
            item.mediaMetadata.description.toString()
        } else null
    }

    private fun hasExtra(key: String?, item: MediaItem?): Boolean {
        return hasExtras(item) && item?.mediaMetadata?.extras!!.containsKey(key)
    }

    private fun hasArtist(mediaItem : MediaItem) : Boolean {
        return mediaItem.mediaMetadata.artist != null
    }

    private fun hasDuration(mediaItem : MediaItem) : Boolean {
        return false
      //  mediaItem.mediaMetadata.
       // return hasExtras(mediaItem) && hasExtra(MediaMetadataCompat.METADATA_KEY_DURATION, mediaItem)
    }

    private fun hasFileCount(mediaItem : MediaItem?) : Boolean {
        return hasExtras(mediaItem) && hasExtra(Constants.FILE_COUNT, mediaItem)
    }

    fun getArtist(item: MediaItem): String {
        return if (hasArtist(item)) {
            item.mediaMetadata.artist.toString()
        } else {
            Constants.UNKNOWN
        }
    }

    @JvmStatic
    fun getAlbumArtPath(item: MediaItem): String? {
        return item.mediaMetadata.artworkUri?.toString()
    }

    @JvmStatic
    fun getDirectoryName(item: MediaItem?): String {
        if (hasExtra(MetaDataKeys.META_DATA_DIRECTORY, item)) {
            val directory = getExtra(MetaDataKeys.META_DATA_DIRECTORY, item) as File?
            if (null != directory) {
                return directory.name
            }
        }
        return Constants.UNKNOWN
    }

    @JvmStatic
    fun getDirectoryPath(item: MediaItem?): String {
        if (hasExtra(MetaDataKeys.META_DATA_DIRECTORY, item)) {
            val directory = getExtra(MetaDataKeys.META_DATA_DIRECTORY, item) as File?
            if (null != directory) {
                return directory.absolutePath
            }
        }
        return Constants.UNKNOWN
    }

    @JvmStatic
    fun getMediaUri(item: MediaItem): Uri? {
        return item.localConfiguration?.uri
    }

    @JvmStatic
    fun getDuration(item: MediaItem): Long {
        return item.mediaMetadata.extras?.get(MetaDataKeys.DURATION) as Long ?: 0L
    }

    @JvmStatic
    fun getFileCount(item : MediaItem?) : Int {
        return if (hasFileCount(item)) getExtra(Constants.FILE_COUNT, item) as Int else -1
    }

    @JvmStatic
    fun getMediaItemType(item: MediaItem?): MediaItemType? {
        return getExtra(Constants.MEDIA_ITEM_TYPE, item) as MediaItemType?
    }

    @JvmStatic
    fun getLibraryId(item: MediaItem?): String? {
        return getExtra(Constants.LIBRARY_ID, item) as String?
    }

    @JvmStatic
    fun getRootMediaItemType(item: MediaItem?): MediaItemType? {
        return getExtra(Constants.ROOT_ITEM_TYPE, item) as MediaItemType?
    }

    fun getAlbumArtUri(song: MediaItem): Uri? {
        return song.mediaMetadata.artworkUri
    }

    @JvmStatic
    fun getAlbumArtImage(song: MediaItem): ByteArray? {
        return song.mediaMetadata.artworkData
    }

    @JvmStatic
    fun getRootTitle(song: MediaItem): String? {
        val extras = song.mediaMetadata.extras
        if (null != extras) {
            val mediaItemType : MediaItemType? = extras.getSerializable(Constants.ROOT_ITEM_TYPE) as MediaItemType
            return mediaItemType?.title
        }
        return null
    }

    @JvmStatic
    fun getEmptyMediaItem() : MediaItem {
        return MediaItemBuilder(EMPTY_MEDIA_ITEM_ID).build()
    }

    fun isEmptyMediaItem(mediaItem: MediaItem?) : Boolean {
        return mediaItem?.mediaId == EMPTY_MEDIA_ITEM_ID
    }
}