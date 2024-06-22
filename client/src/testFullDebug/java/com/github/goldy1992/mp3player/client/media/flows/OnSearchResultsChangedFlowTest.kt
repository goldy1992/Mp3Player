package com.github.goldy1992.mp3player.client.media.flows

import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class OnSearchResultsChangedFlowTest : MediaBrowserListenerFlowTestBase<OnSearchResultsChangedEventHolder>() {

    @Test
    fun testCollectSearchResults() {
        val resultState = initTestFlow(OnSearchResultsChangedEventHolder.DEFAULT)
        OnSearchResultsChangedFlow.create(testScope, addListener, removeListener, collectLambda)
        val expectedQuery = "searchQuery"
        val expectedItemCount = 107
        invoke { it.onSearchResultChanged(controller, expectedQuery, expectedItemCount, MediaLibraryParamUtils.getDefaultLibraryParams()) }
        testScope.advanceUntilIdle()
        val result = resultState.value
        assertEquals(expectedQuery, result.query)
        assertEquals(expectedItemCount, result.itemCount)
    }
}