package com.github.goldy1992.mp3player.service.library.content

import android.provider.MediaStore
import java.util.*

object Projections {
    @JvmField
    val ALBUM_PROJECTION: List<String> = Collections.unmodifiableList(
        listOf(
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums._ID, // ALBUM_ID
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS)
    )
    @JvmField
    val SONG_PROJECTION: List<String> = Collections.unmodifiableList(
            listOf(
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM_ID)
    )

    /**
     * [MediaStore.Audio.Media.DATA] is deprecated for write usage but it's use should be valid for
     * read operations (ref: https://developer.android.com/reference/android/provider/MediaStore.MediaColumns#DATA)
     */
    @JvmField
    val FOLDER_PROJECTION: List<String> = Collections.unmodifiableList(
        listOf(
            MediaStore.Audio.Media.DATA)
    )
}