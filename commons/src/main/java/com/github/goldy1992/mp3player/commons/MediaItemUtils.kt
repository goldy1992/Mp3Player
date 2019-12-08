package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import java.io.File

object MediaItemUtils {
    fun hasExtras(item: MediaBrowserCompat.MediaItem?): Boolean {
        return item != null && item.description.extras != null
    }

    fun hasTitle(item: MediaBrowserCompat.MediaItem?): Boolean {
        return item != null && item.description.title != null
    }

    fun hasDescription(item: MediaBrowserCompat.MediaItem?): Boolean {
        return item != null && item.description.description != null
    }

    @JvmStatic
    fun getExtras(item: MediaBrowserCompat.MediaItem): Bundle? {
        return if (!hasExtras(item)) {
            null
        } else item.description.extras
    }

    fun getExtra(key: String?, item: MediaBrowserCompat.MediaItem?): Any? {
        if (item == null) {
            return null
        }
        val extras = item.description.extras
        return extras?.get(key)
    }

    @JvmStatic
    fun getMediaId(item: MediaBrowserCompat.MediaItem?): String? {
        return item?.description?.mediaId
    }

    @JvmStatic
    fun getTitle(i: MediaBrowserCompat.MediaItem): String? {
        return if (hasTitle(i)) {
            i.description.title.toString()
        } else null
    }

    @JvmStatic
    fun getDescription(item: MediaBrowserCompat.MediaItem): String? {
        return if (hasDescription(item)) {
            item.description.description.toString()
        } else null
    }

    fun hasExtra(key: String?, item: MediaBrowserCompat.MediaItem): Boolean {
        return hasExtras(item) && item.description.extras!!.containsKey(key)
    }

    @JvmStatic
    fun getArtist(item: MediaBrowserCompat.MediaItem?): String? {
        return getExtra(MediaMetadataCompat.METADATA_KEY_ARTIST, item) as String?
    }

    @JvmStatic
    fun getAlbumArtPath(item: MediaBrowserCompat.MediaItem): String? {
        if (hasExtra(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, item)) {
            val uri = getExtra(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, item) as Uri?
            if (uri != null) {
                return uri.toString()
            }
        }
        return null
    }

    @JvmStatic
    fun getDirectoryName(item: MediaBrowserCompat.MediaItem): String? {
        if (hasExtra(MetaDataKeys.META_DATA_DIRECTORY, item)) {
            val directory = getExtra(MetaDataKeys.META_DATA_DIRECTORY, item) as File?
            if (null != directory) {
                return directory.name
            }
        }
        return null
    }

    @JvmStatic
    fun getDirectoryPath(item: MediaBrowserCompat.MediaItem): String? {
        if (hasExtra(MetaDataKeys.META_DATA_DIRECTORY, item)) {
            val directory = getExtra(MetaDataKeys.META_DATA_DIRECTORY, item) as File?
            if (null != directory) {
                return directory.absolutePath
            }
        }
        return null
    }

    @JvmStatic
    fun getMediaUri(item: MediaBrowserCompat.MediaItem): Uri? {
        return item.description.mediaUri
    }

    @JvmStatic
    fun getDuration(item: MediaBrowserCompat.MediaItem?): Long {
        return getExtra(MediaMetadataCompat.METADATA_KEY_DURATION, item) as Long
    }

    @JvmStatic
    fun getMediaItemType(item: MediaBrowserCompat.MediaItem?): MediaItemType? {
        return getExtra(Constants.MEDIA_ITEM_TYPE, item) as MediaItemType?
    }

    @JvmStatic
    fun getLibraryId(item: MediaBrowserCompat.MediaItem?): String? {
        return getExtra(Constants.LIBRARY_ID, item) as String?
    }

    @JvmStatic
    fun getRootMediaItemType(item: MediaBrowserCompat.MediaItem?): MediaItemType? {
        return getExtra(Constants.ROOT_ITEM_TYPE, item) as MediaItemType?
    }

    fun getAlbumArtUri(song: MediaBrowserCompat.MediaItem): Uri? {
        val extras = song.description.extras
        return if (null != extras) {
            extras[MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI] as Uri
        } else null
    }

    @JvmStatic
    fun getAlbumArtImage(song: MediaBrowserCompat.MediaItem): ByteArray {
        val extras = song.description.extras
        return if (null != extras) {
            extras.getSerializable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) as ByteArray
        } else ByteArray(0)
    }

    fun getRootTitle(song: MediaBrowserCompat.MediaItem): String? {
        val extras = song.description.extras
        if (null != extras) {
            val mediaItemType = extras.getSerializable(Constants.ROOT_ITEM_TYPE) as MediaItemType
            return mediaItemType?.title
        }
        return null
    }
}