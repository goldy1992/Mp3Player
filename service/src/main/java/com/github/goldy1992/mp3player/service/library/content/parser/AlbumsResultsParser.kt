package com.github.goldy1992.mp3player.service.library.content.parser

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.*

class AlbumsResultsParser() : ResultsParser() {

    override val type: MediaItemType?
        get() = MediaItemType.ALBUM

    override fun create(cursor: Cursor?, mediaIdPrefix: String?): List<MediaItem> {
        TODO("Not yet implemented")
    }

    override fun create(cursor: Cursor): List<MediaItem> {
        val listToReturn = TreeSet(this)
        while (cursor != null && cursor.moveToNext()) {
            // Log.i(logTag(), "mediaIfPrefix: ${mediaIdPrefix ?: "null"}")
            val mediaItem = buildMediaItem(cursor)
            if (null != mediaItem) {
                listToReturn.add(mediaItem)
            }
        }
        return ArrayList(listToReturn)
    }

    override fun compare(o1: MediaItem?, o2: MediaItem?): Int {
        TODO("Not yet implemented")
    }

    override fun logTag(): String {
        return "AlbumResultsParser"
    }

    private fun buildMediaItem(c: Cursor): MediaItem? {
        val albumIdIndex = c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
        val albumId = if (albumIdIndex >= 0) c.getLong(albumIdIndex) else 0

        val albumTitleIndex = c.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        val albumTitle = if (albumTitleIndex >= 0) c.getString(albumTitleIndex) else Constants.UNKNOWN

        val albumArtistIndex = c.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
        val albumArtist = if (albumArtistIndex >= 0) c.getString(albumArtistIndex) else Constants.UNKNOWN

        val sArtworkUri = Uri.parse(SongResultsParser.ALBUM_ART_URI_PREFIX)
        val albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId)

        val firstYearIndex = c.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR)
        val firstYear = if (firstYearIndex >= 0) c.getLong(firstYearIndex) else 0

        val lastYearIndex = c.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR)
        val lastYear = if (lastYearIndex >= 0) c.getLong(lastYearIndex) else 0
        return MediaItemBuilder(albumId.toString())
            .setAlbumTitle(albumTitle)
            .setAlbumArtist(albumArtist)
            .setAlbumArtUri(albumArtUri)
            .setRecordingYear(firstYear.toInt())
            .setReleaseYear(lastYear.toInt())
            .setMediaItemType(MediaItemType.ALBUM)
            .setIsPlayable(true)
            .build()
    }

}