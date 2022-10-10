package com.github.goldy1992.mp3player.service.library.content.parser

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import android.util.Log
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_NONE
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.uppercaseStringCompare
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import java.io.File
import java.util.*
import javax.inject.Inject

class SongResultsParser
    @Inject
    constructor() : ResultsParser() {

    override fun create(cursor: Cursor?, mediaIdPrefix: String?): List<MediaItem> {
        val listToReturn = TreeSet(this)
        while (cursor != null && cursor.moveToNext()) {
            Log.i(logTag(), "mediaIfPrefix: ${mediaIdPrefix ?: "null"}")
            val mediaItem = buildMediaItem(cursor, mediaIdPrefix)
            if (null != mediaItem) {
                listToReturn.add(mediaItem)
            }
        }
        return ArrayList(listToReturn)
    }

    override fun create(cursor: Cursor): List<MediaItem> {
        return create(cursor, null)
    }

    override val type: MediaItemType?
        get() = MediaItemType.SONG

    private fun buildMediaItem(c: Cursor, libraryIdPrefix: String?): MediaItem? {
        val mediaIdIndex = c.getColumnIndex(MediaStore.Audio.Media._ID)
        val mediaId = if (mediaIdIndex >= 0) c.getString(mediaIdIndex) else Constants.UNKNOWN
        val dataIndex = c.getColumnIndex(MediaStore.Audio.Media.DATA)
        val mediaFilePath = if (dataIndex >= 0) c.getString(dataIndex) else Constants.UNKNOWN
        val mediaFile = File(mediaFilePath)
        val directory = if (!mediaFile.exists()) {
            return null
        } else {
            mediaFile.parentFile
        }
        val mediaUri = Uri.fromFile(mediaFile)
        val durationIndex = c.getColumnIndex(MediaStore.Audio.Media.DURATION)
        val duration = if (durationIndex >= 0) c.getLong(durationIndex) else 0L

        val artistIndex = c.getColumnIndex(MediaStore.Audio.Media.ARTIST)
        val artist = if (artistIndex >= 0) c.getString(artistIndex) else Constants.UNKNOWN

        val titleIndex = c.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val title = if (titleIndex >= 0) c.getString(titleIndex) else Constants.UNKNOWN

        val albumIdIndex = c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
        val albumId = if (albumIdIndex >= 0) c.getLong(albumIdIndex) else 0
        val fileName = mediaFile.name
        val sArtworkUri = Uri.parse(ALBUM_ART_URI_PREFIX)
        val albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId)
        return MediaItemBuilder(mediaId)
                .setMediaUri(mediaUri)
                .setTitle(title)
                .setLibraryId(buildLibraryId(libraryIdPrefix, mediaId))
                .setDuration(duration)
                .setFileName(fileName)
                .setDirectoryFile(directory)
                .setArtist(artist)
                .setMediaItemType(MediaItemType.SONG)
                .setAlbumArtUri(albumArtUri)
                .setIsPlayable(true)
                .setFolderType(FOLDER_TYPE_NONE)
                .build()
    }

    override fun compare(m1: MediaItem, m2: MediaItem): Int {
        val result: Int = uppercaseStringCompare.compare(getTitle(m1), getTitle(m2))
        return if (result == 0) getMediaUri(m1)!!.compareTo(getMediaUri(m2)) else result
    }

    private fun buildLibraryId(mediaIdPrefix: String?, mediaIdSuffix: String): String {
        return if (mediaIdPrefix == null) {
            mediaIdSuffix
        } else {
            mediaIdPrefix + ID_SEPARATOR + mediaIdSuffix
        }
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "Sngs_Res_Prser"
    }

    companion object {
        const val ALBUM_ART_URI_PREFIX = "content://media/external/audio/albumart"
    }
}