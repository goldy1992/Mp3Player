package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest

abstract class ContentResolverRetriever internal constructor(val contentResolver: ContentResolver,
                                                             val resultsParser: ResultsParser,
                                                             val resultsFilter: ResultsFilter?) : ContentRetriever() {
    abstract fun performGetChildrenQuery(id: String?): Cursor?
    abstract val projection: Array<String?>?
    override fun getChildren(request: ContentRequest): List<MediaItem>? {
        val cursor = performGetChildrenQuery(request.queryString)
        val results = resultsParser.create(cursor, request.mediaIdPrefix)
        return if (null != resultsFilter) resultsFilter.filter(request.queryString, results.toMutableList()) else results
    }

}