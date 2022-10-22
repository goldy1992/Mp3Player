package com.github.goldy1992.mp3player.client.data.flows.player

import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
        val isPlayingFLow = IsPlayingFlow(mediaControllerListenableFuture, testScope)
        // await flow to initialise
        advanceUntilIdle()
        var result : Boolean? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            isPlayingFLow.flow().collect {
                result = it
            }
        }
        listener?.onIsPlayingChanged(isPlaying)
        // await listener invocation to complete
        advanceUntilIdle()
        assertEquals(isPlaying, result)
        collectJob.cancel()
    }
}