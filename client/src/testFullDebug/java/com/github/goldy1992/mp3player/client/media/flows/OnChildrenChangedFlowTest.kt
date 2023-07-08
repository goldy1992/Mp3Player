package com.github.goldy1992.mp3player.client.media.flows

import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class OnChildrenChangedFlowTest : MediaBrowserListenerFlowTestBase<OnChildrenChangedEventHolder>() {

    @Test
    fun testSimpleCollect() {
        val resultState = initTestFlow(OnChildrenChangedEventHolder.DEFAULT)
        OnChildrenChangedFlow.create(testScope, addListener, removeListener, collectLambda)
        val expectedId = "testId"
        val expectedItemCount = 76
        invoke { it.onChildrenChanged(controller, expectedId, expectedItemCount, MediaLibraryParamUtils.getDefaultLibraryParams()) }
        testScope.advanceUntilIdle()
        val result = resultState.value
        assertEquals(expectedId, result.parentId)
        assertEquals(expectedItemCount, result.itemCount)
    }
}