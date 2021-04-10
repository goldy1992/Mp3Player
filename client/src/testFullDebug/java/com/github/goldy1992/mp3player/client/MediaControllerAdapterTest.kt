package com.github.goldy1992.mp3player.client

import android.content.Context
import android.media.session.MediaSession
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MediaControllerAdapterTest {

    private val mediaBrowserCompat : MediaBrowserCompat = mock<MediaBrowserCompat>()
    private val mediaControllerCompat : MediaControllerCompat = mock<MediaControllerCompat>()
    private lateinit var mediaControllerAdapter: MediaControllerAdapter
    private lateinit var context: Context
    private lateinit var token: MediaSessionCompat.Token
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        token = createMediaSessionCompatToken()
        whenever(mediaBrowserCompat.sessionToken).thenReturn(token)
        mediaControllerAdapter = spy(MediaControllerAdapter(context, mediaBrowserCompat))
        whenever(mediaControllerAdapter.createMediaController(context, token)).thenReturn(mediaControllerCompat)
        whenever(mediaControllerCompat.metadata).thenReturn(mock<MediaMetadataCompat>())
        whenever(mediaControllerCompat.playbackState).thenReturn(mock<PlaybackStateCompat>())
        whenever(mediaControllerCompat.transportControls).thenReturn(mock<MediaControllerCompat.TransportControls>())
        mediaControllerAdapter.onConnected()
    }

    @Test
    fun testPlay() {
        mediaControllerAdapter.play()
        verify(mediaControllerAdapter, times(1)).play()
    }

    @Test
    fun testPause() {
        mediaControllerAdapter.pause()
        verify(mediaControllerAdapter, times(1)).pause()
    }

    @Test
    fun testSkipToNext() {
        mediaControllerAdapter.skipToNext()
        verify(mediaControllerAdapter, times(1)).skipToNext()
    }

    @Test
    fun testSkipToPrevious() {
        mediaControllerAdapter.skipToPrevious()
        verify(mediaControllerAdapter, times(1)).skipToPrevious()
    }

    @Test
    fun testStop() {
        mediaControllerAdapter.stop()
        verify(mediaControllerAdapter, times(1)).stop()
    }

    @Test
    fun testPrepareFromMediaId() {
        val mediaId = "MEDIA_ID"
        val extras = Bundle()
        mediaControllerAdapter.prepareFromMediaId(mediaId, extras)
        verify(mediaControllerAdapter, times(1)).prepareFromMediaId(mediaId, extras)
    }

    @Test
    fun testPlayFromMediaId() {
        val mediaId = "MEDIA_ID"
        val extras = Bundle()
        mediaControllerAdapter.playFromMediaId(mediaId, extras)
        verify(mediaControllerAdapter, times(1)).playFromMediaId(mediaId, extras)
    }

    @Test
    fun testSeekTo() {
        val position = 23542L
        mediaControllerAdapter.seekTo(position)
        verify(mediaControllerAdapter, times(1)).seekTo(position)
    }

    @Test
    fun testSetRepeatMode() {
        @PlaybackStateCompat.State val repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL
        mediaControllerAdapter.setRepeatMode(repeatMode)
        verify(mediaControllerAdapter, times(1)).setRepeatMode(repeatMode)
    }

    @Test
    fun testGetRepeatMode() {
        val expectedResult = PlaybackStateCompat.REPEAT_MODE_ALL
        mediaControllerAdapter.repeatMode.postValue(expectedResult)
        shadowOf(getMainLooper()).idle()
        val result : Int? = mediaControllerAdapter.repeatMode.value
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetShuffleMode() {
        val expectedResult = PlaybackStateCompat.SHUFFLE_MODE_ALL
        mediaControllerAdapter.shuffleMode.postValue(expectedResult)
        shadowOf(getMainLooper()).idle()
        val result : Int? = mediaControllerAdapter.shuffleMode.value
        assertEquals(expectedResult, result)
    }

//    @Test
//    fun testGetAlbumArtValidUri() {
//        val expectedPath = "mockUriPath"
//        val metadata = MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, expectedPath)
//                .build()
//        whenever(mediaControllerAdapter.metadata).thenReturn(metadata)
//        val result = mediaControllerAdapter.
//        Assert.assertEquals(result?.path, expectedPath)
 //   }
//
//    @Test
//    fun testGetAlbumArtNullUri() {
//        val metadata = MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, null)
//                .build()
//        whenever(mediaControllerAdapter.metadata).thenReturn(metadata)
//        val result = mediaControllerAdapter.currentSongAlbumArtUri
//        Assert.assertNull(result)
//    }
    @Test
    fun testSendCustomAction() {
        val customAction = "DO_SOMETHING"
        val args = Bundle()
        mediaControllerAdapter.sendCustomAction(customAction, args)
        verify(mediaControllerAdapter, times(1)).sendCustomAction(customAction, args)
    }

    @Test
    fun testShuffleMode() {
        @PlaybackStateCompat.State val shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
        mediaControllerAdapter.setShuffleMode(shuffleMode)
        verify(mediaControllerAdapter, times(1)).setShuffleMode(shuffleMode)
    }

//    @Test
//    fun testNullGetPlaybackState() {
//        val result = mediaControllerAdapter.playbackState
//        Assert.assertEquals(0, result.toLong())
//    }

//    @Test
//    fun testGetPlaybackState() {
//        @PlaybackStateCompat.State val state = PlaybackStateCompat.STATE_PAUSED
//        val expectedState = PlaybackStateCompat.Builder()
//                .setState(state, 34L, 0.4f)
//                .build()
//        val mediaControllerCompat = mediaControllerAdapter.mediaController
//        whenever(mediaControllerCompat?.playbackState).thenReturn(expectedState)
//        @PlaybackStateCompat.State val result = mediaControllerAdapter.playbackState
//        Assert.assertEquals(state.toLong(), result.toLong())
//    }

    @Test
    fun testGetPlaybackStateCompatWhenNull() {
        mediaControllerAdapter.playbackState.postValue(null)
        Assert.assertNull(mediaControllerAdapter.playbackState.value)
    }

    @Test
    fun testGetMetadataNullController() {
        mediaControllerAdapter.metadata.postValue(null)
        Assert.assertNull(mediaControllerAdapter.metadata.value)
    }

    @Test
    fun testGetMetadata() {
        val metadata = mock<MediaMetadataCompat>()
        mediaControllerAdapter.metadata.postValue(metadata)
        shadowOf(getMainLooper()).idle()
        assertEquals(metadata, mediaControllerAdapter.metadata.value)
    }

//    @Test
//    fun testDisconnect() {
//        mediaControllerAdapter.disconnect()
//        verify(mediaControllerAdapter.mediaController, times(1))?.unregisterCallback(mediaControllerAdapter)
//    }

    @Test
    fun testCurrentQueuePosition() {
        val expectedQueuePosition = 2
        val expectedQueueId = 13213L
        val mediaDescriptionCompat = MediaDescriptionCompat.Builder().build()
        val expectedQueueItem = MediaSessionCompat.QueueItem(mediaDescriptionCompat, expectedQueueId)
        val inactiveQueueId = 2112L
        val inactiveQueueItem = MediaSessionCompat.QueueItem(mediaDescriptionCompat, inactiveQueueId)
        val playbackStateCompat : PlaybackStateCompat = PlaybackStateCompat.Builder()
                .setActiveQueueItemId(expectedQueueId).build()
        mediaControllerAdapter.playbackState.postValue(playbackStateCompat)
        shadowOf(getMainLooper()).idle()
        val queue : MutableList<MediaSessionCompat.QueueItem> = mutableListOf(inactiveQueueItem, inactiveQueueItem, expectedQueueItem)
        mediaControllerAdapter.onQueueChanged(queue)
        shadowOf(getMainLooper()).idle()

        val result = mediaControllerAdapter.calculateCurrentQueuePosition()
        assertEquals(expectedQueuePosition, result)
    }

    @Test
    fun testCurrentQueuePositionNotFound() {
        val expectedQueuePosition = -1
        val expectedQueueId = 90L
        val playbackStateCompat : PlaybackStateCompat = PlaybackStateCompat.Builder()
                .setActiveQueueItemId(expectedQueueId).build()
        mediaControllerAdapter.playbackState.postValue(playbackStateCompat)
        shadowOf(getMainLooper()).idle()
        mediaControllerAdapter.onQueueChanged(mutableListOf())
        shadowOf(getMainLooper()).idle()
        val result = mediaControllerAdapter.calculateCurrentQueuePosition()
        assertEquals(expectedQueuePosition, result)
    }

    private fun createMediaSessionCompatToken(): MediaSessionCompat.Token
    {
        val mediaSession = MediaSession(InstrumentationRegistry.getInstrumentation().context, "sd")
        val sessionToken = mediaSession.sessionToken
        return MediaSessionCompat.Token.fromToken(sessionToken)
    }
}