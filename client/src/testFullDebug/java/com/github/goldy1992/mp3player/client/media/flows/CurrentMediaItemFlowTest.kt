package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrentMediaItemFlowTest : PlayerMediaFlowTestBase<MediaItem>() {

    @Test
    fun testCurrentMediaItemCollect() {

        val resultState = initTestFlow(MediaItem.EMPTY)


        val expectedMediaItemId = "TestMediaItemId"
        CurrentMediaItemFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val testData = MediaItem.Builder()
            .setMediaId(expectedMediaItemId)
            .setMediaMetadata(MediaMetadata.Builder()
                .setIsBrowsable(false)
                .setIsPlayable(true)
                .build()).build()
        testPlayer.setCurrentMediaItem(testData)

        testScope.advanceUntilIdle()
        val result = resultState.value
        assertEquals(expectedMediaItemId, result.mediaId)

    }

    @Test
    fun testCurrentMediaItemNoCollectWhenNotPlayable() {

        val resultState = initTestFlow(MediaItem.EMPTY)


        val expectedMediaItemId = "TestMediaItemId"
        CurrentMediaItemFlow.create(testScope, controllerFuture, dispatcher, collectLambda)
        val testData = MediaItem.Builder()
            .setMediaId(expectedMediaItemId)
            .setMediaMetadata(MediaMetadata.Builder()
                .setIsBrowsable(false)
                .setIsPlayable(false)
                .build()).build()
        testPlayer.setCurrentMediaItem(testData)

        testScope.advanceUntilIdle()
        val result = resultState.value
        assertNotEquals(expectedMediaItemId, result.mediaId)

    }
}