package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.android.synthetic.main.fragment_album_art.view.*
import kotlinx.android.synthetic.main.song_item_menu.view.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Ignore // TODO: fix
class MySongViewHolderTest : MediaItemViewHolderTestBase<MySongViewHolder>() {
    @Before
    override fun setup() {
        super.setup()
        mediaItemViewHolder = spy(MySongViewHolder(view, albumArtPainter))
    }

    @Test
    fun testBindMediaItemValidTitle() {
        val expectedTitle = "title"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder!!.itemView.title, times(1)).text = expectedTitle
    }

    @Test
    fun testBindMediaItemNullTitleNoFileName() {
        val expectedTitle = Constants.UNKNOWN
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder!!.itemView.title, times(1)).text = expectedTitle
    }

    @Test
    fun testExtractTitleNullTitleFileNameNoExtension() {
        val expectedTitle = "no_extension"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .setFileName(expectedTitle)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder!!.itemView.title, times(1)).text = expectedTitle
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
        verify(mediaItemViewHolder!!.itemView.title, times(1)).text = expectedTitle
    }

    @Test
    fun testSetAlbumArt() {
        val expectedUri = mock<Uri>()
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(expectedUri)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder!!.albumArtPainter, times(1))!!
                .paintOnView(mediaItemViewHolder!!.itemView.albumArt, expectedUri)
    }

    @Test
    fun testSetArtist() {
        val expectedArtist = "artist"
        val mediaItem = MediaItemBuilder("id")
                .setArtist(expectedArtist)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder.itemView.artist, times(1)).text = expectedArtist
    }

    @Test
    fun testSetArtistNull() {
        val expectedArtist = Constants.UNKNOWN
        val mediaItem = MediaItemBuilder("id")
                .setArtist(null)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder.itemView.artist, times(1)).text = expectedArtist
    }

    @Test
    fun testSetDuration() {
        val originalDuration = 34978L
        val expectedDuration = formatTime(originalDuration)
        val mediaItem = MediaItemBuilder("id")
                .setDuration(originalDuration)
                .build()
        mediaItemViewHolder!!.bindMediaItem(mediaItem)
        verify(mediaItemViewHolder.itemView.duration, times(1)).text = expectedDuration
    }
}