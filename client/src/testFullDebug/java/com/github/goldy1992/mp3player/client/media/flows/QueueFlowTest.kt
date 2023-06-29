package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@UnstableApi
@RunWith(RobolectricTestRunner::class)
class QueueFlowTest : PlayerMediaFlowTestBase<QueueState>() {

    @Test
    fun testQueueFlowIsCollected() {
        val resultState = initTestFlow(QueueState.EMPTY)
        QueueFlow.create(controllerFuture, dispatcher, testScope, collectLambda)

        val expectedFirstId = "expectedFirstId"
        val expectedSecondId = "expectedSecondId"
        val mediaItem1 = MediaItem.Builder().setMediaId(expectedFirstId).build()
        val mediaItem2 = MediaItem.Builder().setMediaId(expectedSecondId).build()

        val queue = mutableListOf(mediaItem1, mediaItem2)
        testPlayer.testQueue = queue

        testPlayer.setIsPlaying(true)
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertTrue(result)
    }

}