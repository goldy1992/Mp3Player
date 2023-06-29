package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.Player
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Test

class ShuffleModeFlowTest : PlayerMediaFlowTestBase<Boolean>() {

    @Test
    fun testRepeatModeFlowIsCollected() {
        val resultState = initTestFlow(false)

        ShuffleModeFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val expectedShuffleEnabled = true
        testPlayer.shuffleModeEnabled = expectedShuffleEnabled
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertTrue(result)
    }
}