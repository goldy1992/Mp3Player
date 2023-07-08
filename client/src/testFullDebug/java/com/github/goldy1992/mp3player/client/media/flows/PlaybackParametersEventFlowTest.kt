package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.PlaybackParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class PlaybackParametersEventFlowTest : PlayerMediaFlowTestBase<PlaybackParameters>() {

    @Test
    fun testPlaybackParametersCollected() {
        val resultState = initTestFlow(PlaybackParameters.DEFAULT)

        val expectedSpeed = 1.47f
        val expectedPitch = 2.34f
        PlaybackParametersFlow.create(testScope, controllerFuture, dispatcher, collectLambda)

        val testData = PlaybackParameters(expectedSpeed, expectedPitch)
        testPlayer.playbackParameters = testData
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertEquals(expectedSpeed, result.speed)
        assertEquals(expectedPitch, result.pitch)
    }


}