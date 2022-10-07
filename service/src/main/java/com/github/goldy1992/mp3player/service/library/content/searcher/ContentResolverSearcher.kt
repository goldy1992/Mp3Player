package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser
import com.github.goldy1992.mp3player.service.library.search.SearchDao
import com.github.goldy1992.mp3player.service.library.search.SearchEntity

abstract class ContentResolverSearcher<T : SearchEntity> internal constructor(val contentResolver: ContentResolver,
                                                                               val resultsParser: ResultsParser,
                                                                               val resultsFilter: ResultsFilter?,
                                                                               val searchDatabase: SearchDao<T>) : ContentSearcher {
    abstract val idPrefix: String
    /**
     * @param query the query to search for... assumes that the query as already been normalised
     * @return a list of MediaItem search results
     */
    override fun search(query: String): List<MediaItem>? {
        val cursor = performSearchQuery(query) ?: return emptyList()
        val results = resultsParser.create(cursor, idPrefix)
        return if (null != resultsFilter) resultsFilter.filter(query, results.toMutableList()) else results
    }

    abstract val projection: Array<String?>
    abstract fun performSearchQuery(query: String?): Cursor?

}