package com.github.goldy1992.mp3player.commons

import android.net.Uri
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtPath
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDescription
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDuration
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getExtras
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowMediaItem::class])
class MediaItemUtilsTest {
    @Test
    fun testGetExtrasNull() {
        val mediaItem = MediaItem.EMPTY
        assertNull(getExtras(mediaItem))
    }

    @Test
    fun testGetExtrasNotNull() {
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            title = "algo"
        ).build()
        val result = getExtras(mediaItem)
        assertNotNull(result)
    }

    @Test
    fun testGetMediaIdNotNull() {
        val mediaId = "MEDIA_ID"
        val mediaItem = MediaItemBuilder(mediaId).build()
        assertEquals(mediaId, getMediaId(mediaItem))
    }

    @Test
    fun testGetTitleUnknown() {
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            title = null
        ).build()
        assertUnknown(getTitle(mediaItem))
    }

    @Test
    fun testGetTitleNotNull() {
        val title = "TITLE"
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            title = title
        ).build()
        assertEquals(title, getTitle(mediaItem))
    }

    @Test
    fun testGetArtistReturnsUnknownWhenSetToNull() {
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            artist = null
        ).build()
        assertEquals(Constants.UNKNOWN, getArtist(mediaItem))
    }

    @Test
    fun testGetArtistNotNull() {
        val artist = "ARTIST"
        val mediaItem = MediaItemBuilder(mediaId = "id",
                artist = artist)
                .build()
        assertEquals(artist, getArtist(mediaItem))
    }

    @Test
    fun testGetAlbumArtPathNull() {
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            albumArtUri = null
        ).build()
        assertNull(getAlbumArtPath(mediaItem))
    }

    @Test
    fun testGetAlbumArtPathNotNull() {
        val expectedPath = "sdsad"
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            albumArtUri = Uri.parse(expectedPath)
        ).build()
        assertEquals(expectedPath, getAlbumArtPath(mediaItem))
    }

    @Test
    fun testGetDirectoryNameUnknown() {
        val mediaItem = MediaItemBuilder(
            mediaId = "id"
        ).build()
        assertUnknown(getDirectoryName(mediaItem))
    }

    @Test
    fun testGetDirectoryNameNotNull() {
        val directoryName = "directoryName"
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            file = File("/sdsad/$directoryName"),
            isBrowsable = true
        ).build()
        assertEquals(directoryName, getDirectoryName(mediaItem))
    }

    @Test
    fun testGetMediaUri() {
        val uri = Mockito.mock(Uri::class.java)
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            mediaUri = uri
        ).build()
        val result = getMediaUri(mediaItem)
        assertEquals(uri, result)
    }

    @Test
    fun testGetDuration() {
        val expectedDuration = 231L
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            duration = expectedDuration
        ).build()
        val result = getDuration(mediaItem)
        assertEquals(expectedDuration, result)
    }

    @Test
    fun testGetMediaItemType() {
        val expectedMediaItemType = MediaItemType.SONGS
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            mediaItemType = expectedMediaItemType
        ).build()
        val result = getMediaItemType(mediaItem)
        assertEquals(expectedMediaItemType, result)
    }

    @Test
    fun testGetRootItemType() {
        val expectedMediaItemType = MediaItemType.FOLDER
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            rootMediaItemType = expectedMediaItemType
        ).build()
        val result = getRootMediaItemType(mediaItem)
        assertEquals(expectedMediaItemType, result)
    }

    @Test
    fun testGetDescriptionNull() {
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            description = null
        ).build()
        assertNull(getDescription(mediaItem))
    }

    @Test
    fun testGetDescriptionNotNull() {
        val description = "description"
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            description = description
        ).build()
        assertEquals(description, getDescription(mediaItem))
    }

    @Test
    fun testGetDirectoryPath() {
        val path = "a" + File.pathSeparator + "b" + File.pathSeparator + "c"
        val testFile = File(path)
        val expectedPath = testFile.absolutePath
        val m : MediaItem = MediaItemBuilder(
            mediaId = "id",
            file = testFile
        ).build()
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
        val mediaItem : MediaItem = MediaItemBuilder(
            mediaId = "id",
            albumArtUri = expectedAlbumArtUri
        ).build()
        val result = MediaItemUtils.getAlbumArtUri(mediaItem)
        assertEquals(expectedAlbumArtUri, result)
    }

    @Test
    fun testGetAlbumArtImage() {
        val expectedImage = ByteArray(4)
        val mediaItem : MediaItem = MediaItemBuilder(
            mediaId = "id",
            albumArtData = expectedImage
        ).build()
        val result = MediaItemUtils.getAlbumArtImage(mediaItem)
        assertTrue(expectedImage.contentEquals(result))
    }

    private fun assertUnknown(value : String?) {
        assertEquals(Constants.UNKNOWN, value)
    }
}