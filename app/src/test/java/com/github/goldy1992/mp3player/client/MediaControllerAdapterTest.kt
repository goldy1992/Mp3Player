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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaControllerAdapterTest {
    private var mediaControllerAdapter: MediaControllerAdapter? = null
    @Mock
    private val myMetaDataCallback: MyMetadataCallback? = null
    @Mock
    private val playbackStateCallback: MyPlaybackStateCallback? = null
    private var myMediaControllerCallback: MyMediaControllerCallback? = null
    private var context: Context? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
        myMediaControllerCallback = Mockito.spy(MyMediaControllerCallback(myMetaDataCallback!!, playbackStateCallback!!))
        val token = mediaSessionCompatToken
        mediaControllerAdapter = initialiseMediaControllerAdapter(token)
    }

    @Test
    fun testIsInitialised() {
        Mockito.`when`(mediaControllerAdapter!!.mediaController!!.isSessionReady).thenReturn(true)
        Assert.assertTrue(mediaControllerAdapter!!.isInitialized)
    }

    @Test
    fun testSetMediaSessionTokenWhenAlreadyInitialised() {
        Mockito.`when`(mediaControllerAdapter!!.isInitialized).thenReturn(true)
        val token = mediaSessionCompatToken
        Mockito.reset(mediaControllerAdapter)
        mediaControllerAdapter!!.setMediaToken(token)
        Mockito.verify(mediaControllerAdapter, Mockito.never())!!.init(token)
    }

    @Test
    fun testPlay() {
        mediaControllerAdapter!!.play()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.play()
    }

    @Test
    fun testPause() {
        mediaControllerAdapter!!.pause()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.pause()
    }

    @Test
    fun testSkipToNext() {
        mediaControllerAdapter!!.skipToNext()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.skipToNext()
    }

    @Test
    fun testSkipToPrevious() {
        mediaControllerAdapter!!.skipToPrevious()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.skipToPrevious()
    }

    @Test
    fun testStop() {
        mediaControllerAdapter!!.stop()
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.stop()
    }

    @Test
    fun testPrepareFromMediaId() {
        val mediaId = "MEDIA_ID"
        val extras = Bundle()
        mediaControllerAdapter!!.prepareFromMediaId(mediaId, extras)
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.prepareFromMediaId(mediaId, extras)
    }

    @Test
    fun testPlayFromMediaId() {
        val mediaId = "MEDIA_ID"
        val extras = Bundle()
        mediaControllerAdapter!!.playFromMediaId(mediaId, extras)
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.playFromMediaId(mediaId, extras)
    }

    @Test
    fun testSeekTo() {
        val position = 23542L
        mediaControllerAdapter!!.seekTo(position)
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.seekTo(position)
    }

    @Test
    fun testSetRepeatMode() {
        @PlaybackStateCompat.State val repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL
        mediaControllerAdapter!!.repeatMode = repeatMode
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.repeatMode = repeatMode
    }

    @Test
    fun testGetRepeatMode() {
        val expected = PlaybackStateCompat.REPEAT_MODE_ALL
        Mockito.`when`(mediaControllerAdapter!!.mediaController!!.repeatMode).thenReturn(expected)
        val result = mediaControllerAdapter!!.repeatMode
        Assert.assertEquals(expected.toLong(), result.toLong())
    }

    @Test
    fun testGetShuffleMode() {
        val expected = PlaybackStateCompat.SHUFFLE_MODE_ALL
        Mockito.`when`(mediaControllerAdapter!!.mediaController!!.shuffleMode).thenReturn(expected)
        val result = mediaControllerAdapter!!.shuffleMode
        Assert.assertEquals(expected.toLong(), result.toLong())
    }

    @Test
    fun testGetAlbumArtValidUri() {
        val expectedPath = "mockUriPath"
        val metadata = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, expectedPath)
                .build()
        Mockito.`when`(mediaControllerAdapter!!.metadata).thenReturn(metadata)
        val result = mediaControllerAdapter!!.currentSongAlbumArtUri
        Assert.assertEquals(result!!.path, expectedPath)
    }

    @Test
    fun testSendCustomAction() {
        val customAction = "DO_SOMETHING"
        val args = Bundle()
        mediaControllerAdapter!!.sendCustomAction(customAction, args)
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.sendCustomAction(customAction, args)
    }

    @Test
    fun testShuffleMode() {
        @PlaybackStateCompat.State val shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL
        mediaControllerAdapter!!.shuffleMode = shuffleMode
        Mockito.verify(mediaControllerAdapter, Mockito.times(1))!!.shuffleMode = shuffleMode
    }

    @Test
    fun testNullGetPlaybackState() {
        val result = mediaControllerAdapter!!.playbackState
        Assert.assertEquals(0, result.toLong())
    }

    @Test
    fun testGetPlaybackState() {
        @PlaybackStateCompat.State val state = PlaybackStateCompat.STATE_PAUSED
        val expectedState = PlaybackStateCompat.Builder()
                .setState(state, 34L, 0.4f)
                .build()
        val mediaControllerCompat = mediaControllerAdapter!!.mediaController
        Mockito.`when`(mediaControllerCompat!!.playbackState).thenReturn(expectedState)
        @PlaybackStateCompat.State val result = mediaControllerAdapter!!.playbackState
        Assert.assertEquals(state.toLong(), result.toLong())
    }

    @Test
    fun testGetPlaybackStateCompatWhenNull() {
        mediaControllerAdapter!!.mediaController = null
        Assert.assertNull(mediaControllerAdapter!!.playbackStateCompat)
    }

    @Test
    fun testGetMetadataNullController() {
        mediaControllerAdapter!!.mediaController = null
        Assert.assertNull(mediaControllerAdapter!!.metadata)
    }

    @Test
    fun testGetMetadata() {
        val metadata = Mockito.mock(MediaMetadataCompat::class.java)
        val mockMediaController = Mockito.mock(MediaControllerCompat::class.java)
        Mockito.`when`(mockMediaController.metadata).thenReturn(metadata)
        mediaControllerAdapter!!.mediaController = mockMediaController
        Assert.assertEquals(metadata, mediaControllerAdapter!!.metadata)
    }

    @Test
    fun testRegisterMetaDataListener() {
        val expected = Mockito.mock(MetadataListener::class.java)
        mediaControllerAdapter!!.registerMetaDataListener(expected)
        Mockito.verify(myMetaDataCallback, Mockito.times(1))!!.registerMetaDataListener(expected)
    }

    @Test
    fun testUnregisterMetaDataListener() {
        val expected = Mockito.mock(MetadataListener::class.java)
        mediaControllerAdapter!!.unregisterMetaDataListener(expected)
        Mockito.verify(myMetaDataCallback, Mockito.times(1))!!.removeMetaDataListener(expected)
    }

    @Test
    fun testUnregisterPlaybackStateListener() {
        val expected = Mockito.mock(PlaybackStateListener::class.java)
        mediaControllerAdapter!!.unregisterPlaybackStateListener(expected)
        Mockito.verify(playbackStateCallback, Mockito.times(1))!!.removePlaybackStateListener(expected)
    }

    @Test
    fun testRegisterPlaybackListener() {
        val expected = Mockito.mock(PlaybackStateListener::class.java)
        mediaControllerAdapter!!.registerPlaybackStateListener(expected)
        Mockito.verify(playbackStateCallback, Mockito.times(1))!!.registerPlaybackStateListener(expected)
    }

    @Test
    fun testDisconnect() {
        mediaControllerAdapter!!.disconnect()
        Mockito.verify(mediaControllerAdapter!!.mediaController, Mockito.times(1))!!.unregisterCallback(myMediaControllerCallback!!)
    }

    private fun initialiseMediaControllerAdapter(token: MediaSessionCompat.Token): MediaControllerAdapter {
        val spiedMediaControllerAdapter = Mockito.spy(MediaControllerAdapter(context!!, myMediaControllerCallback))
        Assert.assertFalse(spiedMediaControllerAdapter.isInitialized)
        spiedMediaControllerAdapter.setMediaToken(token)
        Assert.assertEquals(token, spiedMediaControllerAdapter.token)
        val transportControls = Mockito.mock(MediaControllerCompat.TransportControls::class.java)
        Mockito.`when`(spiedMediaControllerAdapter.controller).thenReturn(transportControls)
        val mediaControllerCompat = Mockito.mock(MediaControllerCompat::class.java)
        spiedMediaControllerAdapter.mediaController = mediaControllerCompat
        return spiedMediaControllerAdapter
    }

    private val mediaSessionCompatToken: MediaSessionCompat.Token
        private get() {
            val mediaSession = MediaSession(InstrumentationRegistry.getInstrumentation().context, "sd")
            val sessionToken = mediaSession.sessionToken
            return MediaSessionCompat.Token.fromToken(sessionToken)
        }
}