package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.support.v4.media.MediaBrowserCompat
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

abstract class ContentResolverSearcherTestBase<T : ContentResolverSearcher<*>?> {
    var searcher: T? = null
    var idPrefix: String? = null
    @Mock
    var contentResolver: ContentResolver? = null
    @Mock
    var cursor: Cursor? = null

    companion object {
        const val VALID_QUERY = "VALID_QUERY"
        const val INVALID_QUERY = "INVALID_QUERY"
        var expectedResult: MutableList<MediaBrowserCompat.MediaItem> = ArrayList()

        init {
            expectedResult.add(Mockito.mock(MediaBrowserCompat.MediaItem::class.java))
        }
    }

    abstract fun testGetMediaType()
    abstract fun testSearchValidMultipleArguments()
    @Test
    fun testSearchInvalid() {
        Mockito.`when`<List<MediaBrowserCompat.MediaItem?>>(searcher!!.resultsParser.create(ArgumentMatchers.eq(ArgumentMatchers.any<Cursor>()), idPrefix!!)).thenReturn(expectedResult)
        val result: List<*>? = searcher!!.search(INVALID_QUERY)
        Assert.assertNotEquals(expectedResult, result)
    }
}