package com.github.goldy1992.mp3player.service.player

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.PlaylistManager
import com.google.android.exoplayer2.ExoPlayer
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyMetadataProviderTest {

    private val playlistManager: PlaylistManager = mock<PlaylistManager>()
    private val exoPlayer: ExoPlayer = mock<ExoPlayer>()
    private lateinit var myMetadataProvider: MyMetadataProvider
    @Before
    fun setup() {
        myMetadataProvider = MyMetadataProvider(playlistManager)
    }

    @Test
    fun testGetMetadata() {
        val expectedId = "id"
        val expectedTitle = "title"
        val expectedArtist = "artist"
        val expectedAlbumArt = "/a/b/albumart"
        val expectedDuration = 342L
        val mediaItem = MediaItemBuilder(expectedId)
                .setDuration(expectedDuration)
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .setAlbumArtUri(Uri.parse(expectedAlbumArt))
                .build()
        val index = 7
        Mockito.`when`(exoPlayer!!.currentWindowIndex).thenReturn(index)
        Mockito.`when`(playlistManager!!.getItemAtIndex(index)).thenReturn(mediaItem)
        val result = myMetadataProvider!!.getMetadata(exoPlayer)
        val actualId = result?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
        Assert.assertEquals(expectedId, actualId)
        val actualDuration = result?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
        Assert.assertEquals(expectedDuration, actualDuration)
        val actualTitle = result?.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
        Assert.assertEquals(expectedTitle, actualTitle)
        val actualArtist = result?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
        Assert.assertEquals(expectedArtist, actualArtist)
        val actualAlbumArt = result?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
        Assert.assertEquals(expectedAlbumArt, actualAlbumArt)
    }
}