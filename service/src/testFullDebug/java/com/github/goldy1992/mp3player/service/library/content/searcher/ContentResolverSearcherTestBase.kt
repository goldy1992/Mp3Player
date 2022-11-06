package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import androidx.media3.common.MediaItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
abstract class ContentResolverSearcherTestBase<T : ContentResolverSearcher<*>?> {
    var searcher: T? = null
    var idPrefix: String? = null

    var contentResolver: ContentResolver = mock<ContentResolver>()

    var cursor: Cursor = mock<Cursor>()

    private val testScheduler = TestCoroutineScheduler()
    protected val dispatcher  = StandardTestDispatcher(testScheduler)
    protected val testScope = TestScope(dispatcher)

    companion object {
        const val VALID_QUERY = "VALID_QUERY"
        const val INVALID_QUERY = "INVALID_QUERY"
        var expectedResult: MutableList<MediaItem> = ArrayList()

        init {
            expectedResult.add(mock<MediaItem>())
        }
    }

    abstract fun testGetMediaType()
    abstract fun testSearchValidMultipleArguments()
    @Test
    fun testSearchInvalid() = runTest(dispatcher)  {
        whenever(searcher!!.resultsParser.create(eq(any<Cursor>()), idPrefix!!)).thenReturn(expectedResult)
        var result: List<*>? = searcher!!.search(INVALID_QUERY)
        Assert.assertNotEquals(expectedResult, result)
    }
}