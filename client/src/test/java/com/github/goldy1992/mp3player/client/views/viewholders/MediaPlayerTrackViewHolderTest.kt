package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.view.LayoutInflater
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.databinding.ViewHolderMediaPlayerBinding
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaPlayerTrackViewHolderTest {

    private lateinit var mediaPlayerTrackViewHolder: MediaPlayerTrackViewHolder

    private val albumArtPainter: AlbumArtPainter = mock<AlbumArtPainter>()
    private lateinit var view: ViewHolderMediaPlayerBinding //= mock<ViewHolderMediaPlayerBinding>()
    private val context : Context = InstrumentationRegistry.getInstrumentation().context
     @Before
    fun setup() {
        val inflater = LayoutInflater.from(InstrumentationRegistry.getInstrumentation().context)

            this.view = ViewHolderMediaPlayerBinding.inflate(inflater)
            mediaPlayerTrackViewHolder = MediaPlayerTrackViewHolder(view, albumArtPainter, context)

    }

    @Test
    fun testBindMediaItem() {
        val expectedAlbumArtUri = mock<Uri>()
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(expectedAlbumArtUri)
                .build()
        val queueItem = MediaSessionCompat.QueueItem(mediaItem.description, 1L)
        mediaPlayerTrackViewHolder.bindMediaItem(queueItem)
        verify(albumArtPainter, times(1)).paintOnView(view.albumArt, expectedAlbumArtUri)
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
        verify(albumArtPainter, times(1)).paintOnView(view.albumArt, expectedAlbumArt)
    }
}