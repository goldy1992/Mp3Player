package com.github.goldy1992.mp3player.client

import android.os.Bundle
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Test class for [MediaBrowserAdapter].
 */
class MediaBrowserAdapterTest {

    private lateinit var mediaBrowserAdapter : MediaBrowserAdapter
    private val mockMediaBrowser = mock<MediaBrowser>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val testScheduler = TestCoroutineScheduler()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher  = StandardTestDispatcher(testScheduler)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(dispatcher)


    /** Setup method */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
         mediaBrowserAdapter = MediaBrowserAdapter(Futures.immediateFuture(mockMediaBrowser), testScope, dispatcher)
    }

    /** Tests [MediaBrowserAdapter.subscribe] */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSubscribe() = testScope.runTest {
        val id = "xyz"
        mediaBrowserAdapter.subscribe(id)
        advanceUntilIdle()
        verify(mockMediaBrowser).subscribe(eq(id), any())
    }

    /** Tests [MediaBrowserAdapter.search] */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSearch() = testScope.runTest {
        val query = "query"
        val extras = mock<Bundle>()

        mediaBrowserAdapter.search(query, extras)
        advanceUntilIdle()
        verify(mockMediaBrowser, times(1)).search(eq(query), any())
    }

    /** Tests [MediaBrowserAdapter.search] */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetSearchResults() = testScope.runTest {
        val query = "query"
        val page = 0
        val pageSize = 1
        val expectedMediaId = "expectedMediaId"
        val expectedMediaItem = MediaTestUtils.createTestMediaItem(expectedMediaId)
        whenever(mockMediaBrowser.getSearchResult(eq(query), eq(page), eq(pageSize), any()))
            .thenReturn(
                Futures.immediateFuture(
                    LibraryResult.ofItemList(
                        ImmutableList.of(expectedMediaItem),
                        MediaLibraryService.LibraryParams
                            .Builder()
                            .build())))

        val result = mediaBrowserAdapter.getSearchResults(query, page, pageSize)
        advanceUntilIdle()
        assertEquals(1, result.size)
        val actualMediaItem = result[0]
        assertEquals(expectedMediaId, actualMediaItem.mediaId)
    }

    @Test
    fun testGetLibraryRoot() = testScope.runTest {
        val expectedMediaId = "expectedMediaId"
        val expectedRootMediaItem = MediaTestUtils.createTestMediaItem(expectedMediaId)
        whenever(mockMediaBrowser.getLibraryRoot(any()))
            .thenReturn(
                Futures.immediateFuture(
                    LibraryResult.ofItem(
                        expectedRootMediaItem,
                        MediaTestUtils.getDefaultLibraryParams())))

        val result = mediaBrowserAdapter.getLibraryRoot()

        assertEquals(expectedMediaId, result.mediaId)
    }

    /** Tests [MediaBrowserAdapter.getChildren] */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetChildren() = testScope.runTest {
        val query = "query"
        val page = 0
        val pageSize = 1
        val expectedMediaId = "expectedMediaId"
        val expectedMediaItem = MediaTestUtils.createTestMediaItem(expectedMediaId)
        whenever(mockMediaBrowser.getChildren(eq(query), eq(page), eq(pageSize), any()))
            .thenReturn(
                Futures.immediateFuture(
                    LibraryResult.ofItemList(
                        ImmutableList.of(expectedMediaItem),
                        MediaLibraryService.LibraryParams
                            .Builder()
                            .build())))

        val result = mediaBrowserAdapter.getChildren(query, page, pageSize)
        advanceUntilIdle()
        assertEquals(1, result.size)
        val actualMediaItem = result[0]
        assertEquals(expectedMediaId, actualMediaItem.mediaId)
    }


}