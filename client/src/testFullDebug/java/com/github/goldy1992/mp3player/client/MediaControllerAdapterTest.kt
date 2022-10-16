package com.github.goldy1992.mp3player.client

import androidx.media3.session.MediaController
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
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
//
//    @Test
//    fun testSeekTo() {
//        val position = 23542L
//        mediaControllerAdapter.seekTo(position)
//        verify(mediaControllerAdapter, times(1)).seekTo(position)
//    }
//
//    @Test
//    fun testSetRepeatMode() {
//        @PlaybackStateCompat.RepeatMode val repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL
//        mediaControllerAdapter.setRepeatMode(repeatMode)
//        verify(mediaControllerAdapter, times(1)).setRepeatMode(repeatMode)
//    }
//
//    @Test
//    fun testGetRepeatMode() {
//        val expectedResult = PlaybackStateCompat.REPEAT_MODE_ALL
//        mediaControllerAdapter.repeatMode.postValue(expectedResult)
//        shadowOf(getMainLooper()).idle()
//        val result : Int? = mediaControllerAdapter.repeatMode.value
//        assertEquals(expectedResult, result)
//    }
//
//    @Test
//    fun testGetShuffleMode() {
//        val expectedResult = PlaybackStateCompat.SHUFFLE_MODE_ALL
//        mediaControllerAdapter.shuffleMode.postValue(expectedResult)
//        shadowOf(getMainLooper()).idle()
//        val result : Int? = mediaControllerAdapter.shuffleMode.value
//        assertEquals(expectedResult, result)
//    }
//
//    @Test
//    fun testGetAlbumArtValidUri() {
//        val expectedPath = "mockUriPath"
//        val metadata = MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, expectedPath)
//                .build()
//        mediaControllerAdapter.metadata.postValue(metadata)
//        shadowOf(getMainLooper()).idle()
//        val result = mediaControllerAdapter.getCurrentSongAlbumArtUri()
//        assertEquals(result?.path, expectedPath)
//    }
//
//    @Test
//    fun testGetAlbumArtNullUri() {
//        val metadata = MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, null)
//                .build()
//        mediaControllerAdapter.metadata.postValue(metadata)
//        shadowOf(getMainLooper()).idle()
//        val result = mediaControllerAdapter.getCurrentSongAlbumArtUri()
//        Assert.assertNull(result)
//    }
//
//    @Test
//    fun testSendCustomAction() {
//        val customAction = "DO_SOMETHING"
//        val args = Bundle()
//        mediaControllerAdapter.sendCustomAction(customAction, args)
//        verify(mediaControllerAdapter, times(1)).sendCustomAction(customAction, args)
//    }
//
//    @Test
//    fun testShuffleMode() {
//        @PlaybackStateCompat.ShuffleMode val shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
//        mediaControllerAdapter.setShuffleMode(shuffleMode)
//        verify(mediaControllerAdapter, times(1)).setShuffleMode(shuffleMode)
//    }
//
//    @Test
//    fun testGetPlaybackState() {
//        @PlaybackStateCompat.State val state = PlaybackStateCompat.STATE_PAUSED
//        val expectedState = PlaybackStateCompat.Builder()
//                .setState(state, 34L, 0.4f)
//                .build()
//        mediaControllerAdapter.playbackState.postValue(expectedState)
//        shadowOf(getMainLooper()).idle()
//         val result = mediaControllerAdapter.playbackState
//        Assert.assertEquals(state.toLong(), result.value!!.state.toLong())
//    }
//
//    @Test
//    fun testGetPlaybackStateCompatWhenNull() {
//        mediaControllerAdapter.playbackState.postValue(null)
//        Assert.assertNull(mediaControllerAdapter.playbackState.value)
//    }
//
//    @Test
//    fun testGetMetadataNullController() {
//        mediaControllerAdapter.metadata.postValue(null)
//        Assert.assertNull(mediaControllerAdapter.metadata.value)
//    }
//
//    @Test
//    fun testGetMetadata() {
//        val metadata = mock<MediaMetadataCompat>()
//        mediaControllerAdapter.metadata.postValue(metadata)
//        shadowOf(getMainLooper()).idle()
//        assertEquals(metadata, mediaControllerAdapter.metadata.value)
//    }
//
////    @Test
////    fun testDisconnect() {
////        mediaControllerAdapter.disconnect()
////        verify(mediaControllerAdapter.mediaController, times(1))?.unregisterCallback(mediaControllerAdapter)
////    }
//
//    @Test
//    fun testCurrentQueuePosition() {
//        val expectedQueuePosition = 2
//        val expectedQueueId = 13213L
//        val mediaDescriptionCompat = MediaDescriptionCompat.Builder().build()
//        val expectedQueueItem = MediaSessionCompat.QueueItem(mediaDescriptionCompat, expectedQueueId)
//        val inactiveQueueId = 2112L
//        val inactiveQueueItem = MediaSessionCompat.QueueItem(mediaDescriptionCompat, inactiveQueueId)
//        val playbackStateCompat : PlaybackStateCompat = PlaybackStateCompat.Builder()
//                .setActiveQueueItemId(expectedQueueId).build()
//        mediaControllerAdapter.playbackState.postValue(playbackStateCompat)
//        shadowOf(getMainLooper()).idle()
//        val queue : MutableList<MediaSessionCompat.QueueItem> = mutableListOf(inactiveQueueItem, inactiveQueueItem, expectedQueueItem)
//        mediaControllerAdapter.onQueueChanged(queue)
//        shadowOf(getMainLooper()).idle()
//
//        val result = mediaControllerAdapter.calculateCurrentQueuePosition()
//        assertEquals(expectedQueuePosition, result)
//    }
//
//    @Test
//    fun testCurrentQueuePositionNotFound() {
//        val expectedQueuePosition = -1
//        val expectedQueueId = 90L
//        val playbackStateCompat : PlaybackStateCompat = PlaybackStateCompat.Builder()
//                .setActiveQueueItemId(expectedQueueId).build()
//        mediaControllerAdapter.playbackState.postValue(playbackStateCompat)
//        shadowOf(getMainLooper()).idle()
//        mediaControllerAdapter.onQueueChanged(mutableListOf())
//        shadowOf(getMainLooper()).idle()
//        val result = mediaControllerAdapter.calculateCurrentQueuePosition()
//        assertEquals(expectedQueuePosition, result)
//    }
//
//    private fun createMediaSessionCompatToken(): MediaSessionCompat.Token
//    {
//        val mediaSession = MediaSession(InstrumentationRegistry.getInstrumentation().context, "sd")
//        val sessionToken = mediaSession.sessionToken
//        return MediaSessionCompat.Token.fromToken(sessionToken)
//    }
}