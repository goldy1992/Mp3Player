package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrentPlaylistMetadataFlowTest : PlayerMediaFlowTestBase<MediaMetadata>() {

    @Test
    fun testPlaylistMetadataIsCollected() {
        val resultState = initTestFlow(MediaMetadata.EMPTY)
        val expectedTitle = "expectedTitle"
        CurrentPlaylistMetadataFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val testData = MediaMetadata.Builder().setTitle(expectedTitle).build()
        testPlayer.setPlaylistMetadata(testData)
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertEquals(expectedTitle, result.title)
    }
}