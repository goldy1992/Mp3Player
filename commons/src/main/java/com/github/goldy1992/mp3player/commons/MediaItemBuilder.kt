package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_NONE
import androidx.media3.common.MediaMetadata.FolderType
import java.io.File

class MediaItemBuilder(private val mediaId: String) {

    constructor () : this("defaultMediaId")

    private var description: String? = null
    private var title: String? = null
    private var artist : String? = null
    private var mediaUri: Uri? = null
    private var isPlayable : Boolean = false
    @FolderType
    private var folderType : Int = FOLDER_TYPE_NONE
    private var albumArtUri : Uri? = null
    private var albumArtData : ByteArray? = null
    private val extras: Bundle = Bundle()

    private var flags = 0

    fun setFlags(flags: Int): MediaItemBuilder {
        this.flags = flags
        return this
    }

    fun setDescription(description: String?): MediaItemBuilder {
        this.description = description
        return this
    }

    fun setTitle(title: String?): MediaItemBuilder {
        this.title = title
        return this
    }

    fun setRootItemType(rootItemType: MediaItemType?): MediaItemBuilder {
        extras.putSerializable(Constants.ROOT_ITEM_TYPE, rootItemType)
        return this
    }

    fun setMediaItemType(mediaItemType: MediaItemType?): MediaItemBuilder {
        extras.putSerializable(Constants.MEDIA_ITEM_TYPE, mediaItemType)
        return this
    }

    fun setIsPlayable(isPlayable : Boolean) : MediaItemBuilder {
        this.isPlayable = isPlayable
        return this
    }

    fun setFolderType(@FolderType folderType : Int) : MediaItemBuilder {
        this.folderType = folderType
        return this
    }

    fun setDuration(duration: Long): MediaItemBuilder {
        extras.putLong(MetaDataKeys.DURATION, duration)
        return this
    }

    fun setLibraryId(libraryId: String?): MediaItemBuilder {
        extras.putString(Constants.LIBRARY_ID, libraryId)
        return this
    }

    fun setFileName(fileName: String?): MediaItemBuilder {
        extras.putString(MetaDataKeys.META_DATA_KEY_FILE_NAME, fileName)
        return this
    }

    fun setAlbumArtUri(albumArtUri: Uri?): MediaItemBuilder {
        this.albumArtUri = albumArtUri
        return this
    }

    fun setAlbumArtImage(bitmap: ByteArray?): MediaItemBuilder {
        this.albumArtData = bitmap
        return this
    }

    fun setMediaUri(mediaUri: Uri?): MediaItemBuilder {
        this.mediaUri = mediaUri
        return this
    }

    fun setDirectoryFile(file: File?): MediaItemBuilder {
        extras.putSerializable(MetaDataKeys.META_DATA_DIRECTORY, file)
        return this
    }

    fun setArtist(artist: String?): MediaItemBuilder {
        this.artist = artist
        return this
    }

    fun setFileCount(fileCount : Int) : MediaItemBuilder {
        extras.putInt(Constants.FILE_COUNT, fileCount)
        return this
    }

    fun build(): MediaItem {
        return MediaItem.Builder()
            .setMediaId(mediaId)
            .setUri(mediaUri)
            .setRequestMetadata(RequestMetadata.Builder().setMediaUri(mediaUri).build())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .setFolderType(folderType)
                    .setIsPlayable(isPlayable)
                    .setArtworkUri(albumArtUri)
                    .setArtworkData(this.albumArtData)
                    .setExtras(extras)
                    .build()
            )
            .build()
    }
}