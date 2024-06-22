package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.Player
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

class RepeatModeFlowTest : PlayerMediaFlowTestBase<@Player.RepeatMode Int>() {

    @Test
    fun testRepeatModeFlowIsCollected() {
        val resultState = initTestFlow(Player.REPEAT_MODE_OFF)

        RepeatModeFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val expectedRepeatMode = Player.REPEAT_MODE_ALL
        testPlayer.repeatMode = expectedRepeatMode
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertEquals(expectedRepeatMode, result)
    }
}