package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class MetadataFlowTest : PlayerMediaFlowTestBase<MediaMetadata>() {

    @Test
    fun testMetadataIsCollected() {
        val resultState = initTestFlow(MediaMetadata.EMPTY)
        val expectedTitle = "expectedTitle"
        MetadataFlow.create(testScope, controllerFuture, dispatcher) { v -> resultState.value = v}
        val testData = MediaMetadata.Builder().setTitle(expectedTitle).build()
        testPlayer.mediaMetadata = testData
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertEquals(expectedTitle, result.title)
    }

}