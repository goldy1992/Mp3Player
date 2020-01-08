package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest

abstract class ContentResolverRetriever internal constructor(val contentResolver: ContentResolver,
                                                             val resultsParser: ResultsParser,
                                                             val handler: Handler,
                                                             val resultsFilter: ResultsFilter?) : ContentRetriever() {
    abstract fun performGetChildrenQuery(id: String?): Cursor?
    abstract val projection: Array<String?>?
    override fun getChildren(request: ContentRequest): List<MediaBrowserCompat.MediaItem>? {
        val cursor = performGetChildrenQuery(request.queryString)
        val results = resultsParser.create(cursor, request.mediaIdPrefix)
        return if (null != resultsFilter) resultsFilter.filter(request.queryString, results.toMutableList()) else results
    }

}