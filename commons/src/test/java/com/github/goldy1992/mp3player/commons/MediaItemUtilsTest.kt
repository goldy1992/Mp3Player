package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtPath
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDescription
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDuration
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getExtras
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getLibraryId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class MediaItemUtilsTest {
    @Test
    fun testGetExtrasNull() {
        val mediaDescription = MediaDescriptionCompat.Builder()
                .setMediaId("anId")
                .build()
        val mediaItem = MediaBrowserCompat.MediaItem(mediaDescription, 0)
        Assertions.assertNull(getExtras(mediaItem))
    }

    @Test
    fun testGetExtrasNotNull() {
        val mediaItem = MediaItemBuilder("id")
                .setTitle("algo") // sets an extra
                .build()
        val result = getExtras(mediaItem)
        Assert.assertNotNull(result)
    }

    @Test
    fun testGetMediaIdNotNull() {
        val mediaId = "MEDIA_ID"
        val mediaItem = MediaItemBuilder(mediaId).build()
        Assertions.assertEquals(mediaId, getMediaId(mediaItem))
    }

    @Test
    fun testGetTitleNull() {
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .build()
        Assertions.assertNull(getTitle(mediaItem))
    }

    @Test
    fun testGetTitleNotNull() {
        val title = "TITLE"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(title)
                .build()
        Assertions.assertEquals(title, getTitle(mediaItem))
    }

    @Test
    fun testGetArtistNull() {
        val mediaItem = MediaItemBuilder("id")
                .setArtist(null)
                .build()
        Assertions.assertNull(getArtist(mediaItem))
    }

    @Test
    fun testGetArtistNotNull() {
        val artist = "ARTIST"
        val mediaItem = MediaItemBuilder("id")
                .setArtist(artist)
                .build()
        Assertions.assertEquals(artist, getArtist(mediaItem))
    }

    @Test
    fun testGetAlbumArtPathNull() {
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(null)
                .build()
        Assertions.assertNull(getAlbumArtPath(mediaItem))
    }

    @Test
    fun testGetAlbumArtPathNotNull() {
        val expectedPath = "sdsad"
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(Uri.parse(expectedPath))
                .build()
        Assertions.assertEquals(expectedPath, getAlbumArtPath(mediaItem))
    }

    @Test
    fun testGetDirectoryNameNull() {
        val mediaItem = MediaItemBuilder("id")
                .build()
        Assertions.assertNull(getDirectoryName(mediaItem))
    }

    @Test
    fun testGetDirectoryNameNotNull() {
        val directoryName = "directoryName"
        val mediaItem = MediaItemBuilder("id")
                .setDirectoryFile(File("/sdsad/$directoryName"))
                .build()
        Assertions.assertEquals(directoryName, getDirectoryName(mediaItem))
    }

    @Test
    fun testGetMediaUri() {
        val uri = Mockito.mock(Uri::class.java)
        val mediaItem = MediaItemBuilder("id")
                .setMediaUri(uri)
                .build()
        val result = getMediaUri(mediaItem)
        Assertions.assertEquals(uri, result)
    }

    @Test
    fun testGetDuration() {
        val expectedDuration = 231L
        val mediaItem = MediaItemBuilder("id")
                .setDuration(expectedDuration)
                .build()
        val result = getDuration(mediaItem)
        Assertions.assertEquals(expectedDuration, result)
    }

    @Test
    fun testGetMediaItemType() {
        val expectedMediaItemType = MediaItemType.SONGS
        val mediaItem = MediaItemBuilder("id")
                .setMediaItemType(expectedMediaItemType)
                .build()
        val result = getMediaItemType(mediaItem)
        Assertions.assertEquals(expectedMediaItemType, result)
    }

    @Test
    fun testGetRootItemType() {
        val expectedMediaItemType = MediaItemType.FOLDER
        val mediaItem = MediaItemBuilder("id")
                .setRootItemType(expectedMediaItemType)
                .build()
        val result = getRootMediaItemType(mediaItem)
        Assertions.assertEquals(expectedMediaItemType, result)
    }

    @Test
    fun testGetLibraryId() {
        val expectedLibraryId = "libraryId"
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId(expectedLibraryId)
                .build()
        val result = getLibraryId(mediaItem)
        Assertions.assertEquals(expectedLibraryId, result)
    }

    @Test
    fun testGetDescriptionNull() {
        val mediaItem = MediaItemBuilder("id")
                .setDescription(null)
                .build()
        Assertions.assertNull(getDescription(mediaItem))
    }

    @Test
    fun testGetDescriptionNotNull() {
        val description = "description"
        val mediaItem = MediaItemBuilder("id")
                .setDescription(description)
                .build()
        Assertions.assertEquals(description, getDescription(mediaItem))
    }
}