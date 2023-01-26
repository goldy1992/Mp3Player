package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import javax.inject.Inject

class SongsFromAlbumRetriever@Inject constructor(contentResolver: ContentResolver,
                                                 resultsParser: SongResultsParser) : ContentResolverRetriever(contentResolver, resultsParser, null) {
    override val type: MediaItemType
        get() = MediaItemType.SONG

    override fun performGetChildrenQuery(id: String?): Cursor? {
        val whereArgs = arrayOf(id)
        return contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            WHERE, whereArgs, null)
    }

    override val projection: Array<String?>?
        get() = Projections.SONG_PROJECTION.toTypedArray()

    companion object {
        private const val WHERE = MediaStore.Audio.Albums.ALBUM_ID + " = ? "
    }
}