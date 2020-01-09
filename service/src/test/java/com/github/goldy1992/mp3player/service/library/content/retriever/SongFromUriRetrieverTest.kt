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
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SongFromUriRetrieverTest {
    private var songFromUriRetriever: SongFromUriRetriever? = null

    private val contentResolver: ContentResolver = mock<ContentResolver>()
    
    private val songResultsParser: SongResultsParser = mock<SongResultsParser>()

    private val testUri: Uri = mock<Uri>()

    private val mmr: MediaMetadataRetriever = mock<MediaMetadataRetriever>()
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    @Before
    fun setup() {
        mediaItemTypeIds = MediaItemTypeIds()
        val context = InstrumentationRegistry.getInstrumentation().context
        songFromUriRetriever = SongFromUriRetriever(context, contentResolver!!, songResultsParser!!, mmr!!, mediaItemTypeIds!!)
    }

    @Test
    fun testGetSongWithContentScheme() {
        whenever(testUri!!.scheme).thenReturn(ContentResolver.SCHEME_CONTENT)
        val expectedEmbeddedPic = ByteArray(1)
        whenever(mmr!!.embeddedPicture).thenReturn(expectedEmbeddedPic)
        val expectedTitle = "TITLE"
        whenever(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)).thenReturn(expectedTitle)
        val expectedArtist = "ARTIST"
        whenever(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)).thenReturn(expectedArtist)
        val expectedDuration = 1123L
        whenever(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)).thenReturn(expectedDuration.toString())
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
        val expectedMediaItem = mock<MediaBrowserCompat.MediaItem>()
        val cursor = mock<Cursor>()
        whenever(contentResolver!!.query(any(), any(), eq(null), eq(null), eq(null)))
                .thenReturn(cursor)
        val id = mediaItemTypeIds!!.getId(MediaItemType.SONGS)
        whenever(songResultsParser!!.create(cursor, id!!)).thenReturn(listOf(expectedMediaItem))
        whenever(testUri!!.scheme).thenReturn(ContentResolver.SCHEME_FILE)
        val result = songFromUriRetriever!!.getSong(testUri)
        Assert.assertEquals(expectedMediaItem, result)
    }

    @Test
    fun testUriWithNullScheme() {
        whenever(testUri!!.scheme).thenReturn(null)
        val result = songFromUriRetriever!!.getSong(testUri)
        Assert.assertNull(result)
    }
}