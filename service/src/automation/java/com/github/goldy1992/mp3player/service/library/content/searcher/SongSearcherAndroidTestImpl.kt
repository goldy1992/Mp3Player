package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import com.github.goldy1992.mp3player.service.TestConstants.TEST_DATA_DIR
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.search.Song
import com.github.goldy1992.mp3player.service.library.search.SongDao
import kotlinx.coroutines.CoroutineScope
import org.apache.commons.lang3.StringUtils
import java.util.ArrayList

class SongSearcherAndroidTestImpl

    constructor(contentResolver: ContentResolver,
                resultsParser: SongResultsParser,
                private val mediaItemTypeIds: MediaItemTypeIds,
                songDao: SongDao,
                scope : CoroutineScope)
    : SongSearcher(contentResolver,
        resultsParser,
        mediaItemTypeIds,
        songDao,
        scope) {

    override suspend fun performSearchQuery(query: String?): Cursor? {
        val results: List<Song>? = searchDatabase.query(query)
        val parameters: MutableList<String> = ArrayList()
        val parameterSymbols: MutableList<String?> = ArrayList()
        if (results != null && !results.isEmpty()) {
            for (song in results) {
                parameters.add(song.id)
                parameterSymbols.add(PARAMETER)
            }
        }
        parameters.add("%$TEST_DATA_DIR%")
        val WHERE_CLAUSE = BaseColumns._ID + " IN(" + StringUtils.join(parameterSymbols, ", ") +
                ") AND " + MediaStore.Audio.Media.DATA + " LIKE ? COLLATE NOCASE"
        val WHERE_ARGS = parameters.toTypedArray()
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                WHERE_CLAUSE, WHERE_ARGS, null)
    }
}