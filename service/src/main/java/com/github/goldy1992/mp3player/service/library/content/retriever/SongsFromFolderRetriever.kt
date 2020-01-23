package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.filter.SongsFromFolderResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import javax.inject.Inject
import javax.inject.Named

class SongsFromFolderRetriever @Inject constructor(contentResolver: ContentResolver,
                                                   resultsParser: SongResultsParser,
                                                   @Named("worker") handler: Handler,
                                                   songsFromFolderResultsFilter: SongsFromFolderResultsFilter?) : ContentResolverRetriever(contentResolver, resultsParser, handler, songsFromFolderResultsFilter) {
    override val type: MediaItemType?
        get() = MediaItemType.SONG

    public override fun performGetChildrenQuery(id: String?): Cursor {
        val whereArgs = arrayOf("$id%")
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                WHERE, whereArgs, null)
    }

    override val projection: Array<String?>?
        get() = SONG_PROJECTION.toTypedArray()

    companion object {
        private const val WHERE = MediaColumns.DATA + " LIKE ? "
    }
}