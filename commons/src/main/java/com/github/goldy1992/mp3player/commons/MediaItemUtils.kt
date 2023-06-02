package com.github.goldy1992.mp3player.commons

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.Constants.EMPTY_MEDIA_ITEM_ID
import com.github.goldy1992.mp3player.commons.VersionUtils.isTiramisuOrHigher
import java.io.File
import java.io.Serializable

object MediaItemUtils : LogTagger {
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

    @Suppress("DEPRECATION")
    fun getExtra(key: String?, item: MediaItem?): Any? {
        if (item == null) {
            return null
        }
        val extras = item.mediaMetadata.extras
        return extras?.get(key)
    }


    @Suppress("DEPRECATION")
    private fun <T : Serializable> getSerializableExtra(key: String?, clazz: Class<T>, item: MediaItem?): Any? {
        if (item == null) {
            return null
        }
        val extras = item.mediaMetadata.extras
        return if (isTiramisuOrHigher()) {
            extras?.getSerializable(key, clazz)
        } else {
            extras?.getSerializable(key)
        }
    }

    fun getIntExtra(key: String?, item: MediaItem?): Int? {
        if (item == null) {
            return null
        }
        val extras = item.mediaMetadata.extras
        return extras?.getInt(key)
    }

    fun getStringExtra(key: String?, item: MediaItem?): String? {
        if (item == null) {
            return null
        }
        val extras = item.mediaMetadata.extras
        return extras?.getString(key)
    }

    @JvmStatic
    fun getMediaId(item: MediaItem): String {
        return item.mediaId
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
            val directory = getSerializableExtra(MetaDataKeys.META_DATA_DIRECTORY, File::class.java, item) as File?
            if (null != directory) {
                return directory.name
            }
        }
        return Constants.UNKNOWN
    }

    @JvmStatic
    fun getDirectoryPath(item: MediaItem?): String {
        if (hasExtra(MetaDataKeys.META_DATA_DIRECTORY, item)) {
            val directory = getSerializableExtra(MetaDataKeys.META_DATA_DIRECTORY, File::class.java, item) as File?
            if (null != directory) {
                return directory.absolutePath
            }
        }
        return Constants.UNKNOWN
    }

    @JvmStatic
    fun getDirectoryUri(item: MediaItem?): Uri? {
        if (hasExtra(MetaDataKeys.META_DATA_DIRECTORY, item)) {
            val directory = getSerializableExtra(MetaDataKeys.META_DATA_DIRECTORY, File::class.java, item) as File?
            if (null != directory) {
                return Uri.fromFile(directory)
            }
        }
        return null
    }

    @JvmStatic
    fun getMediaUri(item: MediaItem): Uri? {
        return item.localConfiguration?.uri
    }

    @JvmStatic
    fun getDuration(item: MediaItem): Long {
        Log.i(logTag(), "item: $item")
        Log.i(logTag(), "metadata: ${item.mediaMetadata}")
        Log.i(logTag(), "extras: ${item.mediaMetadata.extras}")
        Log.i(logTag(), "duration: ${item.mediaMetadata.extras?.getLong(MetaDataKeys.DURATION)}")
        return item.mediaMetadata.extras?.getLong(MetaDataKeys.DURATION) ?: 0L
    }

    @JvmStatic
    fun getFileCount(item : MediaItem?) : Int {
        return if (hasFileCount(item)) getIntExtra(Constants.FILE_COUNT, item) ?: -1 else -1
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun getMediaItemType(item: MediaItem): MediaItemType {
        val mediaItemType : MediaItemType? = if (Build.VERSION.SDK_INT >= TIRAMISU) {
            item.mediaMetadata.extras?.getSerializable(
                Constants.MEDIA_ITEM_TYPE,
                MediaItemType::class.java
            )
        } else {
            item.mediaMetadata.extras?.getSerializable(
                Constants.MEDIA_ITEM_TYPE) as MediaItemType?
        }
        if (mediaItemType == null) {
            Log.w(logTag(), "no MediaItemType found for item ${item.mediaId}")
        }
        return mediaItemType ?: MediaItemType.NONE
    }

    @JvmStatic
    fun getRootMediaItemType(item: MediaItem?): MediaItemType? {
        return getSerializableExtra(Constants.ROOT_ITEM_TYPE, MediaItemType::class.java, item) as MediaItemType?
    }

    fun getAlbumArtUri(song: MediaItem): Uri? {
        return song.mediaMetadata.artworkUri
    }

    @JvmStatic
    fun getAlbumArtImage(song: MediaItem): ByteArray? {
        return song.mediaMetadata.artworkData
    }

    @JvmStatic
    fun getEmptyMediaItem() : MediaItem {
        return MediaItemBuilder(EMPTY_MEDIA_ITEM_ID).build()
    }

    fun isEmptyMediaItem(mediaItem: MediaItem?) : Boolean {
        return mediaItem?.mediaId == EMPTY_MEDIA_ITEM_ID
    }

    fun noResultsFound(mediaItems : List<MediaItem>) : Boolean {
        return mediaItems.size == 1 && isEmptyMediaItem(mediaItems[0])
    }

    fun getAlbumTitle(mediaItem: MediaItem) : String {
        return mediaItem.mediaMetadata.albumTitle.toString()
    }

    fun getAlbumArtist(mediaItem: MediaItem) : String {
        return mediaItem.mediaMetadata.albumArtist.toString()
    }

    fun getAlbumRecordingYear(mediaItem: MediaItem) : String {
        return mediaItem.mediaMetadata.recordingYear.toString()
    }

    fun getAlbumReleaseYear(mediaItem: MediaItem) : String {
        return mediaItem.mediaMetadata.releaseYear.toString()
    }


    override fun logTag(): String {
        return "MediaItemUtils"
    }
}