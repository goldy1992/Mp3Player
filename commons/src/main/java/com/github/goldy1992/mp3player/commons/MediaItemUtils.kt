package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import com.github.goldy1992.mp3player.commons.Constants.EMPTY_MEDIA_ITEM_ID
import java.io.File

object MediaItemUtils {
    private fun hasExtras(item: MediaItem?): Boolean {
        return item != null && item.description.extras != null
    }

    private fun hasTitle(item: MediaItem?): Boolean {
        return item != null && item.description.title != null
    }

    private fun hasDescription(item: MediaItem?): Boolean {
        return item != null && item.description.description != null
    }

    @JvmStatic
    fun getExtras(item: MediaItem): Bundle? {
        return if (!hasExtras(item)) {
            null
        } else item.description.extras
    }

    fun getExtra(key: String?, item: MediaItem?): Any? {
        if (item == null) {
            return null
        }
        val extras = item.description.extras
        return extras?.get(key)
    }

    @JvmStatic
    fun getMediaId(item: MediaItem?): String? {
        return item?.description?.mediaId
    }

    @JvmStatic
    fun getTitle(mediaItem: MediaItem): String {
        return if (hasTitle(mediaItem)) {
            mediaItem.description.title.toString()
        } else Constants.UNKNOWN
    }

    @JvmStatic
    fun getDescription(item: MediaItem): String? {
        return if (hasDescription(item)) {
            item.description.description.toString()
        } else null
    }

    private fun hasExtra(key: String?, item: MediaItem?): Boolean {
        return hasExtras(item) && item?.description?.extras!!.containsKey(key)
    }

    private fun hasArtist(mediaItem : MediaItem?) : Boolean {
        return hasExtras(mediaItem) && hasExtra(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaItem!!)
    }

    private fun hasDuration(mediaItem : MediaItem?) : Boolean {
        return hasExtras(mediaItem) && hasExtra(MediaMetadataCompat.METADATA_KEY_DURATION, mediaItem!!)
    }

    @JvmStatic
    fun getArtist(item: MediaItem?): String? {
        return if (hasArtist(item)) {
             getExtra(MediaMetadataCompat.METADATA_KEY_ARTIST, item) as String?
        } else {
            Constants.UNKNOWN
        }
    }

    @JvmStatic
    fun getAlbumArtPath(item: MediaItem): String? {
        if (hasExtra(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, item)) {
            val uri = getExtra(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, item) as Uri?
            if (uri != null) {
                return uri.toString()
            }
        }
        return null
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
        return item.description.mediaUri
    }

    @JvmStatic
    fun getDuration(item: MediaItem?): Long {
        return if (hasDuration(item)) {
            getExtra(MediaMetadataCompat.METADATA_KEY_DURATION, item) as Long
        } else {
            0L
        }
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
        val extras = song.description.extras
        return if (null != extras) {
            extras[MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI] as? Uri
        } else null
    }

    @JvmStatic
    fun getAlbumArtImage(song: MediaItem): ByteArray {
        val extras = song.description.extras
        return if (null != extras) {
            extras.getSerializable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) as ByteArray
        } else ByteArray(0)
    }

    @JvmStatic
    fun getRootTitle(song: MediaItem): String? {
        val extras = song.description.extras
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