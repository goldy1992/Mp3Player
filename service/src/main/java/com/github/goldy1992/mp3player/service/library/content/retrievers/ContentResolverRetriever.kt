package com.github.goldy1992.mp3player.service.library.content.retrievers

import android.content.ContentResolver
import android.database.Cursor
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.content.filter.ResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser

abstract class ContentResolverRetriever internal constructor(val contentResolver: ContentResolver,
                                                             val resultsParser: ResultsParser,
                                                             val resultsFilter: ResultsFilter?) : ContentRetriever() {
    abstract fun performGetChildrenQuery(id: String?): Cursor?
    abstract val projection: Array<String?>?

    override fun getItems(): List<MediaItem> {
        val cursor = performGetChildrenQuery("")
        val results = resultsParser.create(cursor)
        cursor?.close()
        return if (null != resultsFilter) resultsFilter.filter("", results.toMutableList()) ?: emptyList() else results

    }

    override fun getChildren(parentId: String): List<MediaItem> {
        val cursor = performGetChildrenQuery(parentId)
        val results = resultsParser.create(cursor)
        cursor?.close()
        return if (null != resultsFilter) resultsFilter.filter(parentId, results.toMutableList()) ?: emptyList() else results
    }

}