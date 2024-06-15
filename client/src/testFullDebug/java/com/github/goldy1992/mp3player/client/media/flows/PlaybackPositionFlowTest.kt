package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.FlagSet
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@RunWith(RobolectricTestRunner::class)
class PlaybackPositionFlowTest : PlayerMediaFlowTestBase<OnPlaybackPositionChangedEvent>() {

    @Test
    fun  testPlaybackPositionCollected() {
        val resultState = initTestFlow(OnPlaybackPositionChangedEvent.DEFAULT)
        val expectedCurrentPosition = 23409L
        val expectedIsPlaying = true
        PlaybackPositionFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val events = Player.Events(FlagSet.Builder().add(Player.EVENT_PLAYBACK_PARAMETERS_CHANGED).build())
        val testData = OnPlaybackPositionChangedEvent(expectedIsPlaying, expectedCurrentPosition, 0L)
        testPlayer.setPlaybackPositionEvent(testData, events)
        testScope.advanceUntilIdle()
        val result = resultState.value
        assertEquals(expectedCurrentPosition, result.currentPosition)
        assertEquals(expectedIsPlaying, result.isPlaying)
    }


}