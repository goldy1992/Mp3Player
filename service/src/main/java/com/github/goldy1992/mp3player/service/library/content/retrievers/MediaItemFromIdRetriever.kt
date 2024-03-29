package com.github.goldy1992.mp3player.service.library.content.retrievers

import android.content.ContentResolver
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import dagger.hilt.android.scopes.ServiceScoped
import org.apache.commons.collections4.CollectionUtils
import javax.inject.Inject

@ServiceScoped
class MediaItemFromIdRetriever @Inject constructor(private val contentResolver: ContentResolver,
                                                   private val songResultsParser: SongResultsParser) {
    fun getItem(id: Long): MediaItem? {
        val where = BaseColumns._ID + " = ?"
        val whereArgs = arrayOf(id.toString())
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                PROJECTION, where, whereArgs, null)
        if (null != cursor) {
            val results = songResultsParser.create(cursor)
            if (CollectionUtils.isNotEmpty(results)) {
                return results[0]
            }
        }
        return null
    }

    companion object {
        private val PROJECTION: Array<String> = SONG_PROJECTION.toTypedArray()
    }

}