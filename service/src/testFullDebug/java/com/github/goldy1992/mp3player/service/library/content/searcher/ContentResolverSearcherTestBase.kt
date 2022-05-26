package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.support.v4.media.MediaBrowserCompat
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Assert
import org.junit.Test
import java.util.*

abstract class ContentResolverSearcherTestBase<T : ContentResolverSearcher<*>?> {
    var searcher: T? = null
    var idPrefix: String? = null

    var contentResolver: ContentResolver = mock<ContentResolver>()

    var cursor: Cursor = mock<Cursor>()

    companion object {
        const val VALID_QUERY = "VALID_QUERY"
        const val INVALID_QUERY = "INVALID_QUERY"
        var expectedResult: MutableList<MediaBrowserCompat.MediaItem> = ArrayList()

        init {
            expectedResult.add(mock<MediaBrowserCompat.MediaItem>())
        }
    }

    abstract fun testGetMediaType()
    abstract fun testSearchValidMultipleArguments()
    @Test
    fun testSearchInvalid() {
        whenever(searcher!!.resultsParser.create(eq(any<Cursor>()), idPrefix!!)).thenReturn(expectedResult)
        val result: List<*>? = searcher!!.search(INVALID_QUERY)
        Assert.assertNotEquals(expectedResult, result)
    }
}