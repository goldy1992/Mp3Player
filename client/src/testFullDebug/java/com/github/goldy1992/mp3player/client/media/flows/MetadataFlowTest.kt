package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.media.PlayerTestImpl
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

class MetadataFlowTest : PlayerMediaFlowTestBase<MediaMetadata>() {

    @OptIn(ExperimentalCoroutinesApi::class)
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