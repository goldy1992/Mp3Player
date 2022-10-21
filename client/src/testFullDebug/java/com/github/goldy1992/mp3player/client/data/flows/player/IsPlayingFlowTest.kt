package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IsPlayingFlowTest : MediaControllerFlowTestBase() {

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun testIsPlayingFlowWhenPlaying() = runTest(dispatcher) {
        testIsPlaying(true)
    }

    @Test
    fun testIsPlayingFlowNotPlaying() = runTest(dispatcher) {
        testIsPlaying(false)
    }

    private fun testIsPlaying(isPlaying : Boolean) = runTest(dispatcher) {
        val isPlayingFLow = IsPlayingFlow(mediaControllerListenableFuture, testScope, dispatcher)
        // await flow to initialise
        advanceUntilIdle()
        val flowlistener = isPlayingFLow.state

        listener?.onIsPlayingChanged(isPlaying)
        // await listener invocation to complete
        advanceUntilIdle()
        assertEquals(isPlaying, flowlistener.value)
    }

    @Test
    fun testMetadata() = runTest(dispatcher) {
        val expectedArtist = "artist"
        val expectedMetadata = MediaMetadata.Builder().setArtist(expectedArtist).build()
        performTest(expectedMetadata)
    }

    private fun performTest(metadata: MediaMetadata) = runTest(dispatcher) {
        val metadataFlow = MetadataFlow(mediaControllerListenableFuture, testScope, dispatcher)

        advanceUntilIdle()
        val metadataListener = metadataFlow.state
        listener?.onMediaMetadataChanged(metadata)
        advanceUntilIdle()
        val actualMetadata = metadataListener.value
        assertEquals( metadata.artist, actualMetadata.artist)

    }


}