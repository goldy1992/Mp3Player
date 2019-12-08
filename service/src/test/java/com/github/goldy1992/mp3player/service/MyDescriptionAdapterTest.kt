package com.github.goldy1992.mp3player.service

import android.content.Context
import android.media.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.google.android.exoplayer2.ExoPlayer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyDescriptionAdapterTest {
    @Mock
    private val playlistManager: PlaylistManager? = null
    @Mock
    private val player: ExoPlayer? = null
    private var myDescriptionAdapter: MyDescriptionAdapter? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        val token = getMediaSessionCompatToken(context)
        val componentClassMapper = ComponentClassMapper.Builder().build()
        myDescriptionAdapter = MyDescriptionAdapter(context, token, playlistManager!!, componentClassMapper)
    }

    @Test
    fun testGetCurrentContentTitle() {
        val expectedTitle = "title"
        val testItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build()
        val index = 7
        Mockito.`when`(player!!.currentWindowIndex).thenReturn(index)
        Mockito.`when`(playlistManager!!.getItemAtIndex(index)).thenReturn(testItem)
        val result = myDescriptionAdapter!!.getCurrentContentTitle(player)
        Assert.assertEquals(expectedTitle, result)
    }

    private fun getMediaSessionCompatToken(context: Context): MediaSessionCompat.Token {
        val mediaSession = MediaSession(context, "sd")
        val sessionToken = mediaSession.sessionToken
        return MediaSessionCompat.Token.fromToken(sessionToken)
    }
}