package com.github.goldy1992.mp3player.service.library.content.parser

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDuration
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getLibraryId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

/**
 * SongResultsParser testFullUnittest
 */
@RunWith(RobolectricTestRunner::class)
class SongResultsParserTest : ResultsParserTestBase() {

    @Rule
    @JvmField
    var temporaryFolder = TemporaryFolder()

    private val COMMON_TITLE = "a common title"
    private var expectedMediaItem1: MediaBrowserCompat.MediaItem? = null
    private val ALBUM_ID_1 = 2334L
    private val MEDIA_ID_1 = "id1"
    private var expectedMediaItem2: MediaBrowserCompat.MediaItem? = null
    private val ALBUM_ID_2 = 9268L
    private val ID_PREFIX = "sdfa"
    private val MEDIA_ID_2 = "id2"
    @Before
    override fun setup() {
        try {
            val mediaItem1 = temporaryFolder.newFile("uri1")
            val mediaItem2 = temporaryFolder.newFile("uri2")
            expectedMediaItem1 = MediaItemBuilder(MEDIA_ID_1)
                    .setMediaUri(Uri.parse(mediaItem1.absolutePath))
                    .setLibraryId(ID_PREFIX + ID_SEPARATOR + MEDIA_ID_1)
                    .setDuration(23423L)
                    .setArtist("artist1")
                    .setTitle(COMMON_TITLE)
                    .setFileName("fileName1")
                    .setAlbumArtUri(Uri.parse(ALBUM_ID_1.toString() + ""))
                    .build()
            expectedMediaItem2 = MediaItemBuilder(MEDIA_ID_2)
                    .setMediaUri(Uri.parse(mediaItem2.absolutePath))
                    .setLibraryId(ID_PREFIX + ID_SEPARATOR + MEDIA_ID_2)
                    .setDuration(96406L)
                    .setArtist("artist1")
                    .setTitle(COMMON_TITLE)
                    .setFileName("fileName2")
                    .setAlbumArtUri(Uri.parse(ALBUM_ID_2.toString() + ""))
                    .build()
            resultsParser = SongResultsParser()
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
        val mediaItems = getResultsForProjection(SONG_PROJECTION.toTypedArray(), ID_PREFIX)
        Assert.assertEquals(getTitle(expectedMediaItem1!!), getTitle(mediaItems[0]))
        Assert.assertEquals(getArtist(expectedMediaItem1), getArtist(mediaItems[0]))
        Assert.assertEquals(getDuration(expectedMediaItem1), getDuration(mediaItems[0]))
        //assertEquals(MediaItemUtils.getAlbumArtUri(expectedMediaItem1), MediaItemUtils.getAlbumArtUri(mediaItems.get(0)));
        Assert.assertEquals(getLibraryId(expectedMediaItem1), getLibraryId(mediaItems[0]))
        Assert.assertEquals(getTitle(expectedMediaItem2!!), getTitle(mediaItems[1]))
        Assert.assertEquals(getArtist(expectedMediaItem2), getArtist(mediaItems[1]))
        Assert.assertEquals(getDuration(expectedMediaItem2), getDuration(mediaItems[1]))
        //assertEquals(MediaItemUtils.getAlbumArtUri(expectedMediaItem2), MediaItemUtils.getAlbumArtUri(mediaItems.get(1)));
        Assert.assertEquals(getLibraryId(expectedMediaItem2), getLibraryId(mediaItems[1]))
    }

    public override fun createDataSet(): Array<Array<Any?>?> {
        val dataSet: Array<Array<Any?>?> = arrayOfNulls(2)
        dataSet[0] = arrayOf(getMediaUri(expectedMediaItem1!!),
                getDuration(expectedMediaItem1),
                getArtist(expectedMediaItem1),
                getMediaId(expectedMediaItem1),
                getTitle(expectedMediaItem1!!),
                ALBUM_ID_1)
        dataSet[1] = arrayOf(getMediaUri(expectedMediaItem2!!),
                getDuration(expectedMediaItem2),
                getArtist(expectedMediaItem2),
                getMediaId(expectedMediaItem2),
                getTitle(expectedMediaItem2!!),
                ALBUM_ID_2)
        return dataSet
    }
}