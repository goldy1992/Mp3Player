package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.FlagSet
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.github.goldy1992.mp3player.commons.PlayerUtils.buildPlayerEvents
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@UnstableApi
@RunWith(RobolectricTestRunner::class)
class PlayerEventsFlowTest : PlayerMediaFlowTestBase<Player.Events>() {


    private val defaultPlayerEvent = Player.Events(FlagSet.Builder().build())

    @Test
    fun testPlayerEventsCollect() {
        val resultState = initTestFlow(defaultPlayerEvent)

        PlayerEventsFlow.create(testScope, controllerFuture, dispatcher, collectLambda)

        val testEvent1 = Player.EVENT_METADATA
        val testEvent2 = Player.EVENT_MEDIA_METADATA_CHANGED
        val testData = buildPlayerEvents(testEvent1, testEvent2)
        testPlayer.setPlayerEvents(testData)
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertTrue(result.contains(testEvent1))
        assertTrue(result.contains(testEvent2))

    }


}