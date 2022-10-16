package com.github.goldy1992.mp3player.client

import android.os.Bundle
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MediaControllerAdapterTest {


    private lateinit var mediaControllerAdapter : MediaControllerAdapter
    private val mockMediaController = mock<MediaController>()
    private val testScheduler = TestCoroutineScheduler()
    private val dispatcher  = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(dispatcher)

    /** Setup method */
    @Before
    fun setup() {
        mediaControllerAdapter = MediaControllerAdapter(Futures.immediateFuture(mockMediaController), testScope, dispatcher)
    }

    @Test
    fun testPlay() = runTest(dispatcher) {
        mediaControllerAdapter.play()
        advanceUntilIdle()
        verify(mockMediaController, times(1)).play()
    }

    @Test
    fun testPause() = runTest(dispatcher){
        mediaControllerAdapter.pause()
        advanceUntilIdle()
        verify(mockMediaController, times(1)).pause()
    }

    @Test
    fun testSkipToNext() = runTest(dispatcher) {
        mediaControllerAdapter.skipToNext()
        advanceUntilIdle()
        verify(mockMediaController, times(1)).seekToNextMediaItem()
    }

    @Test
    fun testSkipToPrevious() = runTest(dispatcher) {
        mediaControllerAdapter.skipToPrevious()
        advanceUntilIdle()
        verify(mockMediaController, times(1)).seekToPreviousMediaItem()
    }

    @Test
    fun testStop() = runTest(dispatcher) {
        mediaControllerAdapter.stop()
        advanceUntilIdle()
        verify(mockMediaController, times(1)).stop()
    }

    @Test
    fun testPrepareFromMediaId() = runTest(dispatcher) {
        val expectedMediaId = "MEDIA_ID"
        val mediaItem = MediaTestUtils.createTestMediaItem(expectedMediaId)
        mediaControllerAdapter.prepareFromMediaId(mediaItem)
        advanceUntilIdle()
        verify(mockMediaController, times(1)).addMediaItem(mediaItem)
        verify(mockMediaController, times(1)).prepare()
    }

    @Test
    fun testPlayFromMediaId() = runTest(dispatcher) {
        val mediaId = "MEDIA_ID"
        val mediaItem = MediaTestUtils.createTestMediaItem(mediaId)
        mediaControllerAdapter.playFromMediaId(mediaItem)
        advanceUntilIdle()
        verify(mockMediaController, times(1)).addMediaItem(mediaItem)
        verify(mockMediaController, times(1)).prepare()
        verify(mockMediaController, times(1)).play()
    }

    @Test
    fun testSeekTo() = runTest(dispatcher) {
        val position = 23542L
        mediaControllerAdapter.seekTo(position)
        advanceUntilIdle()
        verify(mockMediaController, times(1)).seekTo(position)
    }

    @Test
    fun testSetRepeatMode() = runTest(dispatcher) {
        @Player.RepeatMode val repeatMode = Player.REPEAT_MODE_ALL
        mediaControllerAdapter.setRepeatMode(repeatMode)
        advanceUntilIdle()
        verify(mockMediaController, times(1)).setRepeatMode(repeatMode)
    }

    @Test
    fun testSetShuffleMode() = runTest(dispatcher) {
        val shuffleModeOn = true
        mediaControllerAdapter.setShuffleMode(shuffleModeOn)
        advanceUntilIdle()
        verify(mockMediaController, times(1)).setShuffleModeEnabled(shuffleModeOn)
    }


    @Test
    fun testSendCustomAction() = runTest(dispatcher) {
        val customAction = "DO_SOMETHING"
        val args = Bundle()
        val key = "key"
        val value = "value"
        args.putString(key, value)
        mediaControllerAdapter.sendCustomAction(customAction, args)
        val captor = ArgumentCaptor.forClass(SessionCommand::class.java)
        val bundleCaptor = ArgumentCaptor.forClass(Bundle::class.java)
        verify(mockMediaController, times(1)).sendCustomCommand(captor.capture(), bundleCaptor.capture())
        val sessionCommand = captor.value
        assertEquals(customAction, sessionCommand.customAction)
        assertTrue(sessionCommand.customExtras.containsKey(key))
        assertEquals(value, sessionCommand.customExtras.getString(key))

        val customExtras = bundleCaptor.value
        assertTrue(customExtras.containsKey(key))
        assertEquals(value, customExtras.getString(key))
    }
}