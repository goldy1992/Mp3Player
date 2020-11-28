package com.github.goldy1992.mp3player.service.library.content

import android.provider.MediaStore
import java.util.*

object Projections {
    @JvmField
    val SONG_PROJECTION = Collections.unmodifiableList(
            Arrays.asList(
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM_ID))
    @JvmField
    val FOLDER_PROJECTION = Collections.unmodifiableList(
            Arrays.asList(MediaStore.Audio.Media.DATA))
}