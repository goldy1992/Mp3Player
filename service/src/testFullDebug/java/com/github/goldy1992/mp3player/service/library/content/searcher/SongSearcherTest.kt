package com.github.goldy1992.mp3player.service.library.content.searcher

import android.provider.MediaStore
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.data.search.Song
import com.github.goldy1992.mp3player.service.library.data.search.SongDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SongSearcherTest : ContentResolverSearcherTestBase<SongSearcher?>() {
    private var mediaItemTypeIds: MediaItemTypeIds? = null

    var songDao: SongDao = mock<SongDao>()

    var resultsParser: SongResultsParser = mock<SongResultsParser>()

    @Before
    fun setup() {
        mediaItemTypeIds = MediaItemTypeIds()
        idPrefix = mediaItemTypeIds!!.getId(MediaItemType.SONG)
        searcher = SongSearcher(contentResolver, resultsParser, mediaItemTypeIds!!, songDao, testScope)
    }


    @Test
    override fun testSearchValidMultipleArguments() = runTest(dispatcher) {
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
        @Suppress("UNCHECKED_CAST")
        whenever(songDao.query(ContentResolverSearcherTestBase.Companion.VALID_QUERY)).thenReturn(expectedDbResult as List<Song>)
        val EXPECTED_WHERE = MediaStore.Audio.Media._ID + " IN(?, ?, ?) COLLATE NOCASE"
        val EXPECTED_WHERE_ARGS = arrayOf(id1, id2, id3)
        whenever(contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, searcher!!.projection, EXPECTED_WHERE, EXPECTED_WHERE_ARGS, null))
                .thenReturn(cursor)
        whenever<List<MediaItem?>>(resultsParser.create(cursor)).thenReturn(ContentResolverSearcherTestBase.Companion.expectedResult)
        val result = searcher!!.search(ContentResolverSearcherTestBase.Companion.VALID_QUERY)
        Assert.assertEquals(ContentResolverSearcherTestBase.Companion.expectedResult, result)
    }

    @Test
    override fun testGetMediaType() {
        Assert.assertEquals(MediaItemType.SONGS, searcher!!.searchCategory)
    }
}