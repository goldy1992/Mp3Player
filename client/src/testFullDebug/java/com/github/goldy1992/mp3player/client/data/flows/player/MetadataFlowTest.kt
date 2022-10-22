package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MetadataFlowTest : MediaControllerFlowTestBase() {

    @Before
    override fun setup() {
        whenever(mediaController.mediaMetadata).thenReturn(MediaMetadata.EMPTY)
        super.setup()

    }


    @Test
    fun testMetadata() = testScope.runTest {
        val expectedArtist = "artist"
        val expectedMetadata = MediaMetadata.Builder().setArtist(expectedArtist).build()
        val metadataFlow = MetadataFlow(mediaControllerListenableFuture, testScope)

        var result : MediaMetadata? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            metadataFlow.flow().collect {
                result = it
            }
        }
        advanceUntilIdle()
        listener?.onMediaMetadataChanged(expectedMetadata)
        advanceUntilIdle()
        assertEquals( expectedMetadata.artist, result?.artist)
        collectJob.cancel()
        advanceUntilIdle()
    }




}