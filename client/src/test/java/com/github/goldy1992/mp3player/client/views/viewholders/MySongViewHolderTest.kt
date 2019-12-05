package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MySongViewHolderTest : MediaItemViewHolderTestBase<MySongViewHolder?>() {
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`<Any>(view!!.findViewById(R.id.artist)).thenReturn(Mockito.mock(TextView::class.java))
        Mockito.`when`<Any>(view!!.findViewById(R.id.title)).thenReturn(Mockito.mock(TextView::class.java))
        Mockito.`when`<Any>(view!!.findViewById(R.id.duration)).thenReturn(Mockito.mock(TextView::class.java))
        Mockito.`when`<Any>(view!!.findViewById(R.id.song_item_album_art)).thenReturn(Mockito.mock(ImageView::class.java))
        mediaItemViewHolder = Mockito.spy(MySongViewHolder(view!!, albumArtPainter))
    }

    @Test
    fun testBindMediaItemValidTitle() {
        val expectedTitle = "title"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.title, Mockito.times(1)).text = expectedTitle
    }

    @Test
    fun testBindMediaItemNullTitleNoFileName() {
        val expectedTitle = Constants.UNKNOWN
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.title, Mockito.times(1)).text = expectedTitle
    }

    @Test
    fun testExtractTitleNullTitleFileNameNoExtension() {
        val expectedTitle = "no_extension"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(expectedTitle)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.title, Mockito.times(1)).text = expectedTitle
    }

    @Test
    fun testExtractTitleNullTitleFileNameWithExtension() {
        val expectedTitle = "file_with_extension"
        val fileName = "$expectedTitle.mp3"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(fileName)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.title, Mockito.times(1)).text = expectedTitle
    }

    @Test
    fun testSetAlbumArt() {
        val expectedUri = Mockito.mock(Uri::class.java)
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(expectedUri)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.albumArtPainter, Mockito.times(1))!!
                .paintOnView(mediaItemViewHolder!!.albumArt, expectedUri)
    }

    @Test
    fun testSetArtist() {
        val expectedArtist = "artist"
        val mediaItem = MediaItemBuilder("id")
                .setArtist(expectedArtist)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.artist, Mockito.times(1)).text = expectedArtist
    }

    @Test
    fun testSetArtistNull() {
        val expectedArtist = Constants.UNKNOWN
        val mediaItem = MediaItemBuilder("id")
                .setArtist(null)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.artist, Mockito.times(1)).text = expectedArtist
    }

    @Test
    fun testSetDuration() {
        val originalDuration = 34978L
        val expectedDuration = formatTime(originalDuration)
        val mediaItem = MediaItemBuilder("id")
                .setDuration(originalDuration)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        Mockito.verify(mediaItemViewHolder!!.duration, Mockito.times(1)).text = expectedDuration
    }
}