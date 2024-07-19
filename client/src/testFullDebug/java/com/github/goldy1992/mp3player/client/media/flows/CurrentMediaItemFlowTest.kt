package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrentMediaItemFlowTest : PlayerMediaFlowTestBase<MediaItem>() {

    @Test
    fun testCurrentMediaItemCollect() {

        val resultState = initTestFlow(MediaItem.EMPTY)

        val metadataFlow = MutableStateFlow(MediaMetadata.EMPTY)

        val expectedMediaItemId = "TestMediaItemId"
        CurrentMediaItemFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val testData = MediaItem.Builder().setMediaId(expectedMediaItemId).build()
        testPlayer.setCurrentMediaItem(testData)
        val triggerFlowMetadata = MediaMetadata.Builder().setTitle("new_title").build()
        metadataFlow.value = triggerFlowMetadata

        testScope.advanceUntilIdle()
        val result = resultState.value
        assertEquals(expectedMediaItemId, result.mediaId)

    }
}