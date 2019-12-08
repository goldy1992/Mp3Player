package com.github.goldy1992.mp3player.service.library.content.parser

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.uppercaseStringCompare
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

    override fun create(cursor: Cursor, libraryIdPrefix: String?): List<MediaItem> {
        val listToReturn = TreeSet(this)
        while (cursor != null && cursor.moveToNext()) {
            val mediaItem = buildMediaItem(cursor, libraryIdPrefix!!)
            if (null != mediaItem) {
                listToReturn.add(mediaItem)
            }
        }
        return ArrayList(listToReturn)
    }

    override val type: MediaItemType?
        get() = MediaItemType.SONG

    private fun buildMediaItem(c: Cursor, libraryIdPrefix: String): MediaItem? {
        val mediaId = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID))
        val mediaFilePath = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA))
        val mediaFile = File(mediaFilePath)
        var directory: File? = null
        directory = if (!mediaFile.exists()) {
            return null
        } else {
            mediaFile.parentFile
        }
        val mediaUri = Uri.fromFile(mediaFile)
        val duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION))
        val artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST))
        val title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE))
        val albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
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
                .setFlags(MediaItem.FLAG_PLAYABLE)
                .setAlbumArtUri(albumArtUri)
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

    companion object {
        const val ALBUM_ART_URI_PREFIX = "content://media/external/audio/albumart"
    }
}