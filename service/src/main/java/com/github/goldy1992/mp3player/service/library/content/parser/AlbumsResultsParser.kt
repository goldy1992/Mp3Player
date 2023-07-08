package com.github.goldy1992.mp3player.service.library.content.parser

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.ComparatorUtils
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.scopes.ServiceScoped
import java.util.TreeSet
import javax.inject.Inject

@ServiceScoped
class AlbumsResultsParser

    @Inject
    constructor() : ResultsParser() {

    override val type: MediaItemType
        get() = MediaItemType.ALBUM

    override fun create(cursor: Cursor?): List<MediaItem> {
        val listToReturn = TreeSet(this)
        while (cursor != null && cursor.moveToNext()) {
            val mediaItem = buildMediaItem(cursor)
            listToReturn.add(mediaItem)
        }
        return ArrayList(listToReturn)
    }

    override fun compare(m1: MediaItem?, m2: MediaItem?): Int {
        if (m1 == null && m2 == null) {
            return 0
        } else if (m1 == null) {
            return -1
        } else if (m2 == null) {
            return 1
        }
        val result: Int = ComparatorUtils.Companion.uppercaseStringCompare.compare(
            MediaItemUtils.getAlbumTitle(
                m1
            ), MediaItemUtils.getAlbumTitle(m2)
        )
        return if (result == 0) MediaItemUtils.getAlbumArtist(m1).compareTo(
            MediaItemUtils.getAlbumArtist(
                m2
            )
        ) else result

    }

    override fun logTag(): String {
        return "AlbumResultsParser"
    }

    private fun buildMediaItem(c: Cursor): MediaItem {

        val albumIdIndex = c.getColumnIndex(MediaStore.Audio.Media._ID)
        val albumId = if (albumIdIndex >= 0) c.getLong(albumIdIndex) else 0
        Log.v(logTag(), "buildMediaItem() album_id: $albumId")

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