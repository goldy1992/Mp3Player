package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.github.goldy1992.mp3player.client.databinding.SongItemMenuBinding
import com.github.goldy1992.mp3player.client.databinding.ViewHolderMediaPlayerBinding
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MySongViewHolderTest : MediaItemViewHolderTestBase<MySongViewHolder>() {
    lateinit var view : SongItemMenuBinding

    @Before
    override fun setup() {
        super.setup()
        scenario.onActivity {
            this.view = SongItemMenuBinding.inflate(it.layoutInflater)
            this.mediaItemViewHolder = MySongViewHolder(view, albumArtPainter)
        }
    }

    @Test
    fun testBindMediaItemValidTitle() {
        val expectedTitle = "title"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedTitle, view.title.text)
    }

    @Test
    fun testBindMediaItemNullTitleNoFileName() {
        val expectedTitle = Constants.UNKNOWN
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedTitle, view.title.text)
    }

    @Test
    fun testExtractTitleNullTitleFileNameNoExtension() {
        val expectedTitle = "no_extension"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(expectedTitle)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedTitle, view.title.text)
    }

    @Test
    fun testExtractTitleNullTitleFileNameWithExtension() {
        val expectedTitle = "file_with_extension"
        val fileName = "$expectedTitle.mp3"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(fileName)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedTitle, view.title.text)
    }

    @Test
    fun testSetAlbumArt() {
        val expectedUri = mock<Uri>()
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(expectedUri)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder.albumArtPainter, times(1))!!
                .paintOnView(view.albumArt, expectedUri)
    }

    @Test
    fun testSetArtist() {
        val expectedArtist = "artist"
        val mediaItem = MediaItemBuilder("id")
                .setArtist(expectedArtist)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedArtist, view.artist.text)
    }

    @Test
    fun testSetArtistNull() {
        val expectedArtist = Constants.UNKNOWN
        val mediaItem = MediaItemBuilder("id")
                .setArtist(null)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedArtist, view.artist.text)
    }

    @Test
    fun testSetDuration() {
        val originalDuration = 34978L
        val expectedDuration = formatTime(originalDuration)
        val mediaItem = MediaItemBuilder("id")
                .setDuration(originalDuration)
                .build()
        mediaItemViewHolder.bindMediaItem(mediaItem)
        assertEquals(expectedDuration, view.duration.text)
    }
}