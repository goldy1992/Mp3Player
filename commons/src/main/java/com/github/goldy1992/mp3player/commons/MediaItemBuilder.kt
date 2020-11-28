package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import java.io.File

class MediaItemBuilder(private val mediaId: String) {

    constructor () : this("defaultMediaId")

    private var description: String? = null
    private var title: String? = null
    private var mediaUri: Uri? = null
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

    fun setDuration(duration: Long): MediaItemBuilder {
        extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
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
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArtUri)
        return this
    }

    fun setAlbumArtImage(bitmap: ByteArray?): MediaItemBuilder {
        extras.putSerializable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
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
        extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
        return this
    }

    fun build(): MediaBrowserCompat.MediaItem {
        val mediaDescription = MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(mediaUri)
                .setTitle(title)
                .setDescription(description)
                .setExtras(extras)
                .build()
        return MediaBrowserCompat.MediaItem(mediaDescription, flags)
    }
}