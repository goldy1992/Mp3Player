package com.github.goldy1992.mp3player.client.media.flows

import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsPlayingFlowTest : PlayerMediaFlowTestBase<Boolean>() {

    @Test
    fun testIsPlayingFlowIsCollected() {
        val resultState = initTestFlow(false)
        assertFalse(resultState.value)
        IsPlayingFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        testPlayer.setIsPlaying(true)
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertTrue(result)
    }


}