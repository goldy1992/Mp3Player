package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser
import com.github.goldy1992.mp3player.service.library.data.search.SearchDao
import kotlinx.coroutines.CoroutineScope

abstract class ContentResolverSearcher<T> internal constructor(val contentResolver: ContentResolver,
                                                                              val resultsParser: ResultsParser,
                                                                              val resultsFilter: ResultsFilter?,
                                                                              val searchDatabase: SearchDao<T>,
                                                                              val scope: CoroutineScope) : ContentSearcher {
    abstract val idPrefix: String
    /**
     * @param query the query to search for... assumes that the query as already been normalised
     * @return a list of MediaItem search results
     */
    override suspend fun search(query: String): List<MediaItem>? {
        val cursor = performSearchQuery(query)
        val results = resultsParser.create(cursor)
        cursor?.close()
        return if (null != resultsFilter) resultsFilter.filter(query, results.toMutableList()) else results
    }

    abstract val projection: Array<String?>
    protected abstract suspend fun performSearchQuery(query: String?): Cursor?

}