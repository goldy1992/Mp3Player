package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtImage
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDuration
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SongFromUriRetrieverTest {
    private var songFromUriRetriever: SongFromUriRetriever? = null
    @Mock
    private val contentResolver: ContentResolver? = null
    @Mock
    private val songResultsParser: SongResultsParser? = null
    @Mock
    private val testUri: Uri? = null
    @Mock
    private val mmr: MediaMetadataRetriever? = null
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mediaItemTypeIds = MediaItemTypeIds()
        val context = InstrumentationRegistry.getInstrumentation().context
        songFromUriRetriever = SongFromUriRetriever(context, contentResolver!!, songResultsParser!!, mmr!!, mediaItemTypeIds!!)
    }

    @Test
    fun testGetSongWithContentScheme() {
        Mockito.`when`(testUri!!.scheme).thenReturn(ContentResolver.SCHEME_CONTENT)
        val expectedEmbeddedPic = ByteArray(1)
        Mockito.`when`(mmr!!.embeddedPicture).thenReturn(expectedEmbeddedPic)
        val expectedTitle = "TITLE"
        Mockito.`when`(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)).thenReturn(expectedTitle)
        val expectedArtist = "ARTIST"
        Mockito.`when`(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)).thenReturn(expectedArtist)
        val expectedDuration = 1123L
        Mockito.`when`(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)).thenReturn(expectedDuration.toString())
        val result = songFromUriRetriever!!.getSong(testUri)
        val actualEmbeddedPicture = getAlbumArtImage(result!!)
        Assert.assertEquals(expectedEmbeddedPic, actualEmbeddedPicture)
        val actualTitle = getTitle(result)
        Assert.assertEquals(expectedTitle, actualTitle)
        val actualArtist = getArtist(result)
        Assert.assertEquals(expectedArtist, actualArtist)
        val actualDuration = getDuration(result)
        Assert.assertEquals(expectedDuration, actualDuration)
        val actualMediaUri = getMediaUri(result)
        Assert.assertEquals(testUri, actualMediaUri)
    }

    @Test
    fun testGetSongWithNonContentScheme() {
        val expectedMediaItem = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
        val cursor = Mockito.mock(Cursor::class.java)
        Mockito.`when`(contentResolver!!.query(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(cursor)
        val id = mediaItemTypeIds!!.getId(MediaItemType.SONGS)
        Mockito.`when`(songResultsParser!!.create(cursor, id!!)).thenReturn(listOf(expectedMediaItem))
        Mockito.`when`(testUri!!.scheme).thenReturn(ContentResolver.SCHEME_FILE)
        val result = songFromUriRetriever!!.getSong(testUri)
        Assert.assertEquals(expectedMediaItem, result)
    }

    @Test
    fun testUriWithNullScheme() {
        Mockito.`when`(testUri!!.scheme).thenReturn(null)
        val result = songFromUriRetriever!!.getSong(testUri)
        Assert.assertNull(result)
    }
}