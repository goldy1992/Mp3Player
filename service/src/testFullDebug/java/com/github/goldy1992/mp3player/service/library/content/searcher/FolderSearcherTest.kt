package com.github.goldy1992.mp3player.service.library.content.searcher

import android.provider.MediaStore
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.search.Folder
import com.github.goldy1992.mp3player.service.library.search.FolderDao
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
class FolderSearcherTest : ContentResolverSearcherTestBase<FolderSearcher?>() {
    private lateinit var filter: FolderSearchResultsFilter

    var resultsParser: FolderResultsParser = mock<FolderResultsParser>()
    private var mediaItemTypeIds: MediaItemTypeIds? = null

    var folderDao: FolderDao = mock<FolderDao>()

    @Before
    fun setup() {
        mediaItemTypeIds = MediaItemTypeIds()
        idPrefix = mediaItemTypeIds!!.getId(MediaItemType.FOLDER)
        filter = mock<FolderSearchResultsFilter>()
        whenever(filter.filter(ContentResolverSearcherTestBase.Companion.VALID_QUERY, ContentResolverSearcherTestBase.Companion.expectedResult)).thenReturn(ContentResolverSearcherTestBase.Companion.expectedResult)
        searcher = spy(FolderSearcher(contentResolver, resultsParser, filter, mediaItemTypeIds!!, folderDao, testScope))
    }


    @Test
    override fun testSearchValidMultipleArguments() = runTest(dispatcher) {
        val expectedDbResult: MutableList<Folder?> = ArrayList()
        val id1 = "id1"
        val id2 = "id2"
        val id3 = "id3"
        val value1 = "value1"
        val value2 = "value2"
        val value3 = "value3"
        val folder1 = Folder(id1, value1)
        val folder2 = Folder(id2, value2)
        val folder3 = Folder(id3, value3)
        expectedDbResult.add(folder1)
        expectedDbResult.add(folder2)
        expectedDbResult.add(folder3)
        @Suppress("UNCHECKED_CAST")
        whenever(folderDao.query(ContentResolverSearcherTestBase.Companion.VALID_QUERY)).thenReturn(expectedDbResult as List<Folder>)
        val EXPECTED_WHERE = (MediaStore.Audio.Media.DATA + " LIKE ? OR "
                + MediaStore.Audio.Media.DATA + " LIKE ? OR "
                + MediaStore.Audio.Media.DATA + " LIKE ? COLLATE NOCASE")
        val EXPECTED_WHERE_ARGS = arrayOf(
                searcher!!.likeParam(id1),
                searcher!!.likeParam(id2),
                searcher!!.likeParam(id3))
        whenever(contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, searcher!!.projection, EXPECTED_WHERE, EXPECTED_WHERE_ARGS, null))
                .thenReturn(cursor)
        whenever<List<MediaItem?>>(resultsParser.create(cursor, idPrefix!!)).thenReturn(ContentResolverSearcherTestBase.Companion.expectedResult)
        val result = searcher!!.search(ContentResolverSearcherTestBase.Companion.VALID_QUERY)
        Assert.assertEquals(ContentResolverSearcherTestBase.Companion.expectedResult, result)
    }

    @Test
    override fun testGetMediaType() {
        Assert.assertEquals(MediaItemType.FOLDERS, searcher!!.searchCategory)
    }
}