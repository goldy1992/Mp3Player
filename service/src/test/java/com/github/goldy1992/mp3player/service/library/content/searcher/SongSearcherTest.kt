package com.github.goldy1992.mp3player.service.library.content.searcher

import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.search.Song
import com.github.goldy1992.mp3player.service.library.search.SongDao
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class SongSearcherTest : ContentResolverSearcherTestBase<SongSearcher?>() {
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    @Mock
    var songDao: SongDao? = null
    @Mock
    var resultsParser: SongResultsParser? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mediaItemTypeIds = MediaItemTypeIds()
        idPrefix = mediaItemTypeIds!!.getId(MediaItemType.SONG)
        searcher = Mockito.spy(SongSearcher(contentResolver!!, resultsParser!!, mediaItemTypeIds!!, songDao!!))
    }

    @Test
    override fun testSearchValidMultipleArguments() {
        val expectedDbResult: MutableList<Song?> = ArrayList()
        val id1 = "id1"
        val id2 = "id2"
        val id3 = "id3"
        val song1 = Song(id1, "song1")
        val song2 = Song(id2, "song2")
        val song3 = Song(id3, "song3")
        expectedDbResult.add(song1)
        expectedDbResult.add(song2)
        expectedDbResult.add(song3)
        Mockito.`when`(songDao!!.query(ContentResolverSearcherTestBase.Companion.VALID_QUERY)).thenReturn(expectedDbResult as List<Song>)
        val EXPECTED_WHERE = MediaStore.Audio.Media._ID + " IN(?, ?, ?) COLLATE NOCASE"
        val EXPECTED_WHERE_ARGS = arrayOf(id1, id2, id3)
        Mockito.`when`(contentResolver!!.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, searcher!!.projection, EXPECTED_WHERE, EXPECTED_WHERE_ARGS, null))
                .thenReturn(cursor)
        Mockito.`when`<List<MediaBrowserCompat.MediaItem?>>(resultsParser!!.create(cursor!!, idPrefix!!)).thenReturn(ContentResolverSearcherTestBase.Companion.expectedResult)
        val result = searcher!!.search(ContentResolverSearcherTestBase.Companion.VALID_QUERY)
        Assert.assertEquals(ContentResolverSearcherTestBase.Companion.expectedResult, result)
    }

    @Test
    override fun testGetMediaType() {
        Assert.assertEquals(MediaItemType.SONGS, searcher!!.searchCategory)
    }
}