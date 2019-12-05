package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaPlayerTrackViewHolderTest {
    private var mediaPlayerTrackViewHolder: MediaPlayerTrackViewHolder? = null
    @Mock
    private val albumArtPainter: AlbumArtPainter? = null
    @Mock
    private val titleView: TextView? = null
    @Mock
    private val artistView: TextView? = null
    @Mock
    private val albumArtView: ImageView? = null
    @Mock
    private val view: View? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`<Any?>(view!!.findViewById(R.id.songTitle)).thenReturn(titleView)
        Mockito.`when`<Any?>(view.findViewById(R.id.songArtist)).thenReturn(artistView)
        Mockito.`when`<Any?>(view.findViewById(R.id.albumArt)).thenReturn(albumArtView)
        mediaPlayerTrackViewHolder = MediaPlayerTrackViewHolder(view, albumArtPainter!!)
    }

    @Test
    fun testBindMediaItem() {
        val expectedTitle = "TITLE"
        val expectedArtist = "ARTIST"
        val expectedAlbumArtUri = Mockito.mock(Uri::class.java)
        val mediaItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .setAlbumArtUri(expectedAlbumArtUri)
                .build()
        val queueItem = MediaSessionCompat.QueueItem(mediaItem.description, 1L)
        mediaPlayerTrackViewHolder!!.bindMediaItem(queueItem)
        Mockito.verify(titleView, Mockito.times(1))!!.text = expectedTitle
        Mockito.verify(artistView, Mockito.times(1))!!.text = expectedArtist
        Mockito.verify(albumArtPainter, Mockito.times(1))!!.paintOnView(albumArtView, expectedAlbumArtUri)
    }

    @Test
    fun testBindMediaItemByteAlbumArt() {
        val expectedAlbumArt = ByteArray(1)
        val mediaItem = MediaItemBuilder("id")
                .setTitle("title")
                .setArtist("artist")
                .setAlbumArtImage(expectedAlbumArt)
                .build()
        val queueItem = MediaSessionCompat.QueueItem(mediaItem.description, 1L)
        mediaPlayerTrackViewHolder!!.bindMediaItem(queueItem)
        Mockito.verify(albumArtPainter, Mockito.times(1))!!.paintOnView(albumArtView, expectedAlbumArt)
    }
}