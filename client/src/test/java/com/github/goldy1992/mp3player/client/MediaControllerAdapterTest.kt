package com.github.goldy1992.mp3player.client

import android.content.Context
import android.media.session.MediaSession
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaControllerAdapterTest {

    private val myMetaDataCallback: MyMetadataCallback = mock<MyMetadataCallback>()
    private val playbackStateCallback: MyPlaybackStateCallback = mock<MyPlaybackStateCallback>()
    
    private lateinit var mediaControllerAdapter: MediaControllerAdapter
    private lateinit var myMediaControllerCallback: MyMediaControllerCallback
    private lateinit var context: Context
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        myMediaControllerCallback = spy(MyMediaControllerCallback(myMetaDataCallback, playbackStateCallback))
        val token = mediaSessionCompatToken
        mediaControllerAdapter = initialiseMediaControllerAdapter(token)
    }

    @Test
    fun testIsInitialised() {
        whenever(mediaControllerAdapter.mediaController?.isSessionReady).thenReturn(true)
        Assert.assertTrue(mediaControllerAdapter.isInitialized)
    }

    @Test
    fun testSetMediaSessionTokenWhenAlreadyInitialised() {
        whenever(mediaControllerAdapter.isInitialized).thenReturn(true)
        val token = mediaSessionCompatToken
        reset(mediaControllerAdapter)
        mediaControllerAdapter.setMediaToken(token)
        verify(mediaControllerAdapter, never()).init(token)
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
        val expected = PlaybackStateCompat.REPEAT_MODE_ALL
        whenever(mediaControllerAdapter.mediaController?.repeatMode).thenReturn(expected)
        val result = mediaControllerAdapter.getRepeatMode()!!
        Assert.assertEquals(expected.toLong(), result.toLong())
    }

    @Test
    fun testGetShuffleMode() {
        val expected = PlaybackStateCompat.SHUFFLE_MODE_ALL
        whenever(mediaControllerAdapter.mediaController?.shuffleMode).thenReturn(expected)
        val result = mediaControllerAdapter.getShuffleMode()!!
        Assert.assertEquals(expected.toLong(), result.toLong())
    }

    @Test
    fun testGetAlbumArtValidUri() {
        val expectedPath = "mockUriPath"
        val metadata = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, expectedPath)
                .build()
        whenever(mediaControllerAdapter.metadata).thenReturn(metadata)
        val result = mediaControllerAdapter.currentSongAlbumArtUri
        Assert.assertEquals(result?.path, expectedPath)
    }

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

    @Test
    fun testNullGetPlaybackState() {
        val result = mediaControllerAdapter.playbackState
        Assert.assertEquals(0, result.toLong())
    }

    @Test
    fun testGetPlaybackState() {
        @PlaybackStateCompat.State val state = PlaybackStateCompat.STATE_PAUSED
        val expectedState = PlaybackStateCompat.Builder()
                .setState(state, 34L, 0.4f)
                .build()
        val mediaControllerCompat = mediaControllerAdapter.mediaController
        whenever(mediaControllerCompat?.playbackState).thenReturn(expectedState)
        @PlaybackStateCompat.State val result = mediaControllerAdapter.playbackState
        Assert.assertEquals(state.toLong(), result.toLong())
    }

    @Test
    fun testGetPlaybackStateCompatWhenNull() {
        mediaControllerAdapter.mediaController = null
        Assert.assertNull(mediaControllerAdapter.playbackStateCompat)
    }

    @Test
    fun testGetMetadataNullController() {
        mediaControllerAdapter.mediaController = null
        Assert.assertNull(mediaControllerAdapter.metadata)
    }

    @Test
    fun testGetMetadata() {
        val metadata = mock<MediaMetadataCompat>()
        val mockMediaController = mock<MediaControllerCompat>()
        whenever(mockMediaController.metadata).thenReturn(metadata)
        mediaControllerAdapter.mediaController = mockMediaController
        Assert.assertEquals(metadata, mediaControllerAdapter.metadata)
    }

    @Test
    fun testRegisterMetaDataListener() {
        val expected = mock<MetadataListener>()
        mediaControllerAdapter.registerMetaDataListener(expected)
        verify(myMetaDataCallback, times(1)).registerMetaDataListener(expected)
    }

    @Test
    fun testUnregisterMetaDataListener() {
        val expected = mock<MetadataListener>()
        mediaControllerAdapter.unregisterMetaDataListener(expected)
        verify(myMetaDataCallback, times(1)).removeMetaDataListener(expected)
    }

    @Test
    fun testUnregisterPlaybackStateListener() {
        val expected = mock<PlaybackStateListener>()
        mediaControllerAdapter.unregisterPlaybackStateListener(expected)
        verify(playbackStateCallback, times(1)).removePlaybackStateListener(expected)
    }

    @Test
    fun testRegisterPlaybackListener() {
        val expected = mock<PlaybackStateListener>()
        mediaControllerAdapter.registerPlaybackStateListener(expected)
        verify(playbackStateCallback, times(1)).registerPlaybackStateListener(expected)
    }

    @Test
    fun testDisconnect() {
        mediaControllerAdapter.disconnect()
        verify(mediaControllerAdapter.mediaController, times(1))?.unregisterCallback(myMediaControllerCallback)
    }

    private fun initialiseMediaControllerAdapter(token: MediaSessionCompat.Token): MediaControllerAdapter {
        val spiedMediaControllerAdapter = spy(MediaControllerAdapter(context, myMediaControllerCallback))
        Assert.assertFalse(spiedMediaControllerAdapter.isInitialized)
        spiedMediaControllerAdapter.setMediaToken(token)
        Assert.assertEquals(token, spiedMediaControllerAdapter.token)
        val transportControls = mock<MediaControllerCompat.TransportControls>()
        whenever(spiedMediaControllerAdapter.controller).thenReturn(transportControls)
        val mediaControllerCompat = mock<MediaControllerCompat>()
        spiedMediaControllerAdapter.mediaController = mediaControllerCompat
        return spiedMediaControllerAdapter
    }

    private val mediaSessionCompatToken: MediaSessionCompat.Token
        get() {
            val mediaSession = MediaSession(InstrumentationRegistry.getInstrumentation().context, "sd")
            val sessionToken = mediaSession.sessionToken
            return MediaSessionCompat.Token.fromToken(sessionToken)
        }
}