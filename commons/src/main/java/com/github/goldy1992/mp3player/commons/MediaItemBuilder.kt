package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata
import java.io.File
data class MediaItemBuilder(
    private val mediaId: String = "defaultMediaId",
    private val libraryId: String = "defaultLibraryId",
    private val description: String? = null,
    private val duration: Long = 1000L,
    private val title: String? = null,
    private val artist : String? = null,
    private val albumArtist : String? = null,
    private val albumTitle : String? = null,
    private val recordingYear : Int = -1,
    private val releaseYear : Int = -1,
    private val mediaUri: Uri? = null,
    private val isPlayable : Boolean = false,
    private val isBrowsable: Boolean = false,
    private val albumArtUri : Uri? = null,
    private val albumArtData : ByteArray? = null,
    private val file: File? = null,
    private val fileCount: Int = 0,
    private val rootMediaItemType: MediaItemType = MediaItemType.NONE,
    private val mediaItemType: MediaItemType = MediaItemType.NONE,
) {

    companion object {
        private val DEFAULT = MediaItemBuilder()
    }

    fun build(): MediaItem {
        val extras = Bundle()
        extras.putString(Constants.LIBRARY_ID, libraryId)
        extras.putLong(MetaDataKeys.DURATION, duration)
        extras.putSerializable(Constants.MEDIA_ITEM_TYPE, mediaItemType)
        extras.putSerializable(Constants.ROOT_ITEM_TYPE, rootMediaItemType)
        if (file != null) {
            extras.putString(MetaDataKeys.META_DATA_KEY_FILE_NAME, file.name)
            if (file.isDirectory || isBrowsable) {
                extras.putSerializable(MetaDataKeys.META_DATA_DIRECTORY, file)
            }
            extras.putInt(Constants.FILE_COUNT, fileCount)

        }
        return MediaItem.Builder()
            .setMediaId(mediaId)
            .setUri(mediaUri)
            .setRequestMetadata(RequestMetadata.Builder().setMediaUri(mediaUri).build())
            .setMediaMetadata(
                MediaMetadata.Builder()
                .setTitle(title)
                .setDescription(description)
                .setArtist(artist)
                .setAlbumArtist(albumArtist)
                .setAlbumTitle(albumTitle)
                .setRecordingYear(recordingYear)
                .setReleaseYear(releaseYear)
                .setIsBrowsable(isBrowsable)
                .setIsPlayable(isPlayable)
                .setArtworkUri(albumArtUri)
                .setArtworkData(this.albumArtData, MediaMetadata.PICTURE_TYPE_MEDIA)
                .setExtras(extras)
                .build()
            )
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaItemBuilder

        if (mediaId != other.mediaId) return false
        if (libraryId != other.libraryId) return false
        if (description != other.description) return false
        if (duration != other.duration) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (albumArtist != other.albumArtist) return false
        if (albumTitle != other.albumTitle) return false
        if (recordingYear != other.recordingYear) return false
        if (releaseYear != other.releaseYear) return false
        if (mediaUri != other.mediaUri) return false
        if (isPlayable != other.isPlayable) return false
        if (isBrowsable != other.isBrowsable) return false
        if (albumArtUri != other.albumArtUri) return false
        if (albumArtData != null) {
            if (other.albumArtData == null) return false
            if (!albumArtData.contentEquals(other.albumArtData)) return false
        } else if (other.albumArtData != null) return false
        if (file != other.file) return false
        if (fileCount != other.fileCount) return false
        if (rootMediaItemType != other.rootMediaItemType) return false
        if (mediaItemType != other.mediaItemType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mediaId.hashCode()
        result = 31 * result + libraryId.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + duration.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (artist?.hashCode() ?: 0)
        result = 31 * result + (albumArtist?.hashCode() ?: 0)
        result = 31 * result + (albumTitle?.hashCode() ?: 0)
        result = 31 * result + recordingYear
        result = 31 * result + releaseYear
        result = 31 * result + (mediaUri?.hashCode() ?: 0)
        result = 31 * result + isPlayable.hashCode()
        result = 31 * result + isBrowsable.hashCode()
        result = 31 * result + (albumArtUri?.hashCode() ?: 0)
        result = 31 * result + (albumArtData?.contentHashCode() ?: 0)
        result = 31 * result + (file?.hashCode() ?: 0)
        result = 31 * result + fileCount
        result = 31 * result + rootMediaItemType.hashCode()
        result = 31 * result + mediaItemType.hashCode()
        return result
    }

}