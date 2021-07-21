package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
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
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
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
        val mediaItem = MediaItem(mediaDescription, 0)
        assertNull(getExtras(mediaItem))
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
        assertEquals(mediaId, getMediaId(mediaItem))
    }

    @Test
    fun testGetTitleWhenSetAsNull() {
        val mediaItem = MediaItemBuilder("id")
                .setTitle(null)
                .build()
        assertUnknown(getTitle(mediaItem))
    }

    @Test
    fun testGetTitleNotNull() {
        val title = "TITLE"
        val mediaItem = MediaItemBuilder("id")
                .setTitle(title)
                .build()
        assertEquals(title, getTitle(mediaItem))
    }

    @Test
    fun testGetArtistNull() {
        val mediaItem = MediaItemBuilder("id")
                .setArtist(null)
                .build()
        assertNull(getArtist(mediaItem))
    }

    @Test
    fun testGetArtistNotNull() {
        val artist = "ARTIST"
        val mediaItem = MediaItemBuilder("id")
                .setArtist(artist)
                .build()
        assertEquals(artist, getArtist(mediaItem))
    }

    @Test
    fun testGetAlbumArtPathNull() {
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(null)
                .build()
        assertNull(getAlbumArtPath(mediaItem))
    }

    @Test
    fun testGetAlbumArtPathNotNull() {
        val expectedPath = "sdsad"
        val mediaItem = MediaItemBuilder("id")
                .setAlbumArtUri(Uri.parse(expectedPath))
                .build()
        assertEquals(expectedPath, getAlbumArtPath(mediaItem))
    }

    @Test
    fun testGetDirectoryNameNull() {
        val mediaItem = MediaItemBuilder("id")
                .build()
        assertUnknown(getDirectoryName(mediaItem))
    }

    @Test
    fun testGetDirectoryNameNotNull() {
        val directoryName = "directoryName"
        val mediaItem = MediaItemBuilder("id")
                .setDirectoryFile(File("/sdsad/$directoryName"))
                .build()
        assertEquals(directoryName, getDirectoryName(mediaItem))
    }

    @Test
    fun testGetMediaUri() {
        val uri = Mockito.mock(Uri::class.java)
        val mediaItem = MediaItemBuilder("id")
                .setMediaUri(uri)
                .build()
        val result = getMediaUri(mediaItem)
        assertEquals(uri, result)
    }

    @Test
    fun testGetDuration() {
        val expectedDuration = 231L
        val mediaItem = MediaItemBuilder("id")
                .setDuration(expectedDuration)
                .build()
        val result = getDuration(mediaItem)
        assertEquals(expectedDuration, result)
    }

    @Test
    fun testGetMediaItemType() {
        val expectedMediaItemType = MediaItemType.SONGS
        val mediaItem = MediaItemBuilder("id")
                .setMediaItemType(expectedMediaItemType)
                .build()
        val result = getMediaItemType(mediaItem)
        assertEquals(expectedMediaItemType, result)
    }

    @Test
    fun testGetRootItemType() {
        val expectedMediaItemType = MediaItemType.FOLDER
        val mediaItem = MediaItemBuilder("id")
                .setRootItemType(expectedMediaItemType)
                .build()
        val result = getRootMediaItemType(mediaItem)
        assertEquals(expectedMediaItemType, result)
    }

    @Test
    fun testGetLibraryId() {
        val expectedLibraryId = "libraryId"
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId(expectedLibraryId)
                .build()
        val result = getLibraryId(mediaItem)
        assertEquals(expectedLibraryId, result)
    }

    @Test
    fun testGetDescriptionNull() {
        val mediaItem = MediaItemBuilder("id")
                .setDescription(null)
                .build()
        assertNull(getDescription(mediaItem))
    }

    @Test
    fun testGetDescriptionNotNull() {
        val description = "description"
        val mediaItem = MediaItemBuilder("id")
                .setDescription(description)
                .build()
        assertEquals(description, getDescription(mediaItem))
    }

    @Test
    fun testGetDirectoryPath() {
        val path = "a" + File.pathSeparator + "b" + File.pathSeparator + "c"
        val testFile = File(path)
        val expectedPath = testFile.absolutePath
        val m : MediaItem = MediaItemBuilder("id")
                .setDirectoryFile(testFile)
                .build()
        val result = MediaItemUtils.getDirectoryPath(m)
        assertEquals(expectedPath, result)
    }

    @Test
    fun testGetNullAlbumArtUri() {
        val m : MediaItem = MediaItemBuilder()
                .build()
        val result = MediaItemUtils.getAlbumArtUri(m)
        assertNull(result)
    }

    @Test
    fun testGetAlbumArtUri() {
        val expectedAlbumArtUri : Uri = mock<Uri>()
        val mediaItem : MediaItem = MediaItemBuilder()
                .setAlbumArtUri(expectedAlbumArtUri)
                .build()
        val result = MediaItemUtils.getAlbumArtUri(mediaItem)
        assertEquals(expectedAlbumArtUri, result)
    }

    @Test
    fun testGetAlbumArtImage() {
        val expectedImage = ByteArray(4)
        val mediaItem : MediaItem = MediaItemBuilder()
                .setAlbumArtImage(expectedImage)
                .build()
        val result = MediaItemUtils.getAlbumArtImage(mediaItem)
        assertEquals(expectedImage, result)
    }

    @Test
    fun testGetRootTitle() {
        val rootItemType : MediaItemType = MediaItemType.FOLDERS
        val expectedRootTitle : String? = rootItemType.title
        val mediaItem : MediaItem = MediaItemBuilder()
                .setRootItemType(rootItemType)
                .build()
        val result = MediaItemUtils.getRootTitle(mediaItem)

        assertEquals(expectedRootTitle, result)
    }

    private fun assertUnknown(value : String?) {
        assertEquals(Constants.UNKNOWN, value)
    }
}