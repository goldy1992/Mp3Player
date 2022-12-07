package com.github.goldy1992.mp3player.client.data.flows.mediabrowser

import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnChildrenChangedFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnChildrenChangedFlowTest : MediaBrowserFlowTestBase() {


    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun testOnChildrenFlowChanged() = runTest {
        val expectedParentId = "expectedParentId"
        val expectedItemCount = 44
        val params = MediaLibraryService.LibraryParams.Builder().build()
        val onChildrenChangedFlow = OnChildrenChangedFlow(asyncMediaBrowserListener, testScope)

        var result : OnChildrenChangedEventHolder? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            onChildrenChangedFlow.flow.collect {
                result = it
            }
        }
        testScope.advanceUntilIdle()
        advanceUntilIdle()
        listener?.onChildrenChanged(mediaBrowser, expectedParentId, expectedItemCount, params)
        testScope.advanceUntilIdle()
        advanceUntilIdle()

        Assert.assertEquals( expectedParentId, result?.parentId)
        Assert.assertEquals( expectedItemCount, result?.itemCount)
        collectJob.cancel()
        advanceUntilIdle()
    }
}