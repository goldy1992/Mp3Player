package com.github.goldy1992.mp3player.service.library.content.parser

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDuration
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser.Companion.ALBUM_ART_URI_PREFIX
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import java.io.IOException

/**
 * SongResultsParser testFullDebug
 */
@RunWith(RobolectricTestRunner::class)
class SongResultsParserTest : ResultsParserTestBase() {

    val contentResolver = mock<ContentResolver>()

    @Rule
    @JvmField
    var temporaryFolder = TemporaryFolder()

    private val COMMON_TITLE = "a common title"
    private lateinit var expectedMediaItem1: MediaItem
    private val ALBUM_ID_1 = 2334L
    private val MEDIA_ID_1 = "1234"
    private lateinit var expectedMediaItem2: MediaItem
    private val ALBUM_ID_2 = 9268L
    private val ID_PREFIX = "sdfa"
    private val MEDIA_ID_2 = "5678"
    @Before
    override fun setup() {
        try {
            val mediaItem1 = temporaryFolder.newFile("uri1")
            val mediaItem2 = temporaryFolder.newFile("uri2")
            expectedMediaItem1 = MediaItemBuilder(MEDIA_ID_1)
                    .setMediaUri(Uri.parse(mediaItem1.absolutePath))
                    .setDuration(23423L)
                    .setArtist("artist1")
                    .setTitle(COMMON_TITLE)
                    .setFileName("fileName1")
                    .setAlbumArtUri(Uri.parse(ALBUM_ID_1.toString() + ""))
                    .build()
            expectedMediaItem2 = MediaItemBuilder(MEDIA_ID_2)
                    .setMediaUri(Uri.parse(mediaItem2.absolutePath))
                    .setDuration(96406L)
                    .setArtist("artist1")
                    .setTitle(COMMON_TITLE)
                    .setFileName("fileName2")
                    .setAlbumArtUri(Uri.parse(ALBUM_ID_2.toString() + ""))
                    .build()
            resultsParser = SongResultsParser(contentResolver)
        } catch (e: IOException) {
            e.printStackTrace()
            Assert.fail()
        }
    }

    @Test
    override fun testGetType() {
        assertEquals(MediaItemType.SONG, resultsParser!!.type)
    }

    /**
     * GIVEN: a cursor with 2 results
     * AND: given that both results have the same title
     * WHEN: the parser is run
     * THEN: a list of MediaItems is returned with the expected values
     * AND: the list is of size 2
     */
    @Test
    fun testCreate() {
        val mediaItems = getResultsForProjection(SONG_PROJECTION.toTypedArray())
        assertEquals(getTitle(expectedMediaItem1), getTitle(mediaItems[0]))
        assertEquals(getArtist(expectedMediaItem1), getArtist(mediaItems[0]))
        assertEquals(getDuration(expectedMediaItem1), getDuration(mediaItems[0]))
        assertEquals(
            ContentUris.withAppendedId(Uri.parse(ALBUM_ART_URI_PREFIX),
            expectedMediaItem1.mediaMetadata.artworkUri.toString().toLong()), MediaItemUtils.getAlbumArtUri(mediaItems[0])
        )
        assertEquals(getTitle(expectedMediaItem2), getTitle(mediaItems[1]))
        assertEquals(getArtist(expectedMediaItem2), getArtist(mediaItems[1]))
        assertEquals(getDuration(expectedMediaItem2), getDuration(mediaItems[1]))
        assertEquals(ContentUris.withAppendedId(Uri.parse(ALBUM_ART_URI_PREFIX), expectedMediaItem2.mediaMetadata.artworkUri.toString().toLong()), MediaItemUtils.getAlbumArtUri(mediaItems[1]));
    }

    public override fun createDataSet(): Array<Array<Any?>?> {
        val dataSet: Array<Array<Any?>?> = arrayOfNulls(2)
        dataSet[0] = arrayOf(getMediaUri(expectedMediaItem1),
                getDuration(expectedMediaItem1),
                getArtist(expectedMediaItem1),
                getMediaId(expectedMediaItem1),
                getTitle(expectedMediaItem1),
                ALBUM_ID_1)
        dataSet[1] = arrayOf(getMediaUri(expectedMediaItem2),
                getDuration(expectedMediaItem2),
                getArtist(expectedMediaItem2),
                getMediaId(expectedMediaItem2),
                getTitle(expectedMediaItem2),
                ALBUM_ID_2)
        return dataSet
    }
}