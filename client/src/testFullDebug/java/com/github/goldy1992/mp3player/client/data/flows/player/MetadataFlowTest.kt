package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.ui.flows.player.MetadataFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
    fun testMetadata() = runTest {
        val expectedArtist = "artist"
        val expectedMetadata = MediaMetadata.Builder().setArtist(expectedArtist).build()
        val metadataFlow = MetadataFlow(mediaControllerListenableFuture, testScope)

        var result : MediaMetadata? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            metadataFlow.flow().collect {
                result = it
            }
        }
        testScope.advanceUntilIdle()
        listener?.onMediaMetadataChanged(expectedMetadata)
        advanceUntilIdle()
        testScope.advanceUntilIdle()
        assertEquals( expectedMetadata.artist, result?.artist)
        collectJob.cancel()
        testScope.advanceUntilIdle()
    }

}