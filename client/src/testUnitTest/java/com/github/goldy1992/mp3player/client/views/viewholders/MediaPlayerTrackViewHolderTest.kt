package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.views.SquareImageView
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.view_holder_media_player.view.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaPlayerTrackViewHolderTest {

    private lateinit var mediaPlayerTrackViewHolder: MediaPlayerTrackViewHolder

    private val albumArtPainter: AlbumArtPainter = mock<AlbumArtPainter>()
    private val titleView: TextView = mock<TextView>()
    private val artistView: TextView = mock<TextView>()
    private val albumArtView: SquareImageView = mock<SquareImageView>()
    private val view: View = mock<View>()

    @Before
    fun setup() {
        whenever<Any?>(view.songTitle).thenReturn(titleView)
        whenever<Any?>(view.songArtist).thenReturn(artistView)
        whenever<Any?>(view.albumArt).thenReturn(albumArtView)
        mediaPlayerTrackViewHolder = MediaPlayerTrackViewHolder(view, albumArtPainter)

    }

    @Test
    fun testBindMediaItem() {
        val expectedTitle = "TITLE"
        val expectedArtist = "ARTIST"
        val expectedAlbumArtUri = mock<Uri>()
        val mediaItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .setAlbumArtUri(expectedAlbumArtUri)
                .build()
        val queueItem = MediaSessionCompat.QueueItem(mediaItem.description, 1L)
        mediaPlayerTrackViewHolder.bindMediaItem(queueItem)
        verify(titleView, times(1)).text = expectedTitle
        verify(artistView, times(1)).text = expectedArtist
        verify(albumArtPainter, times(1)).paintOnView(albumArtView, expectedAlbumArtUri)
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
        mediaPlayerTrackViewHolder.bindMediaItem(queueItem)
        verify(albumArtPainter, times(1)).paintOnView(albumArtView, expectedAlbumArt)
    }
}