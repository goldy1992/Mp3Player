package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
import com.github.goldy1992.mp3player.commons.PlayerUtils.buildPlayerEvents
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@UnstableApi
@RunWith(RobolectricTestRunner::class)
class QueueFlowTest : PlayerMediaFlowTestBase<OnQueueChangedEventHolder>() {

    @Test
    fun testQueueFlowIsCollected() {
        val resultState = initTestFlow(OnQueueChangedEventHolder.EMPTY)
        QueueFlow.create(controllerFuture, dispatcher, testScope, collectLambda)

        val expectedFirstId = "expectedFirstId"
        val expectedSecondId = "expectedSecondId"
        val mediaItem1 = MediaItem.Builder().setMediaId(expectedFirstId).build()
        val mediaItem2 = MediaItem.Builder().setMediaId(expectedSecondId).build()

        val queue = mutableListOf(mediaItem1, mediaItem2)
        testPlayer.testQueue = queue


        testPlayer.setPlayerEvents(buildPlayerEvents(Player.EVENT_TRACKS_CHANGED))
        testScope.advanceUntilIdle()


        val result = resultState.value
        assertEquals(2, result.items.size)
        assertEquals(mediaItem1.mediaId, result.items[0].mediaId)
        assertEquals(mediaItem2.mediaId, result.items[1].mediaId)
    }

}