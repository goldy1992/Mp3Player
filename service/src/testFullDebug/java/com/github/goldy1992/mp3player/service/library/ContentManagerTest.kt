package com.github.goldy1992.mp3player.service.library

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequestParser
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.MediaItemFromIdRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ContentManagerTest {
    private var contentManager: ContentManager? = null

    private val contentRequestParser: ContentRequestParser = mock<ContentRequestParser>()

    private val contentRetrievers: ContentRetrievers = mock<ContentRetrievers>()

    private val contentSearchers: ContentSearchers = mock<ContentSearchers>()
    
    private val rootRetriever: RootRetriever = mock<RootRetriever>()
    
    private val rootItem: MediaItem = mock<MediaItem>()
    
    private val mediaItemFromIdRetriever: MediaItemFromIdRetriever = mock<MediaItemFromIdRetriever>()
    
    private val songFromUriRetriever: SongFromUriRetriever = mock<SongFromUriRetriever>()

    private val testScheduler = TestCoroutineScheduler()
    private val dispatcher  = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(dispatcher)


    @Before
    fun setup() {

        whenever(contentRetrievers.root).thenReturn(rootRetriever)
        whenever(rootRetriever.getRootItem(any<MediaItemType>())).thenReturn(rootItem)
        contentManager = ContentManager(contentRetrievers,
                contentSearchers,
                contentRequestParser,
                songFromUriRetriever,
                mediaItemFromIdRetriever)
    }

    @Test
    fun testGetChildren() {
        val contentRetrieverId = "id"
        val expectedList: List<MediaItem> = ArrayList()
        val contentRetriever = mock<ContentRetriever>()
        whenever(contentRetrievers[contentRetrieverId]).thenReturn(contentRetriever)
        val contentRequest = ContentRequest("", contentRetrieverId, null)
        whenever(contentRequestParser.parse(contentRetrieverId)).thenReturn(contentRequest)
        whenever(contentRetriever.getChildren(contentRequest)).thenReturn(expectedList)
        val result = contentManager!!.getChildren(contentRetrieverId)
        Assert.assertEquals(expectedList, result)
    }

    @Test
    fun testGetChildrenNull() {
        val incorrectId = "incorrectId"
        val contentRequest = ContentRequest("", incorrectId, null)
        whenever(contentRequestParser.parse(incorrectId)).thenReturn(contentRequest)
        val result = contentManager!!.getChildren(incorrectId)
        Assert.assertNull(result)
    }

    /**
     * EXPECTED:
     * 1) SONGS_TITLE
     * 2) SONG 1
     * 3) SONG 2
     * 4) FOLDERS TITLE
     * 5) FOLDER 1
     * i.e result size 5
     */
    @Test
    fun testValidSearchQuery() = runTest(dispatcher) {
        testSearch(LOWER_CASE_VALID_QUERY, 5)
    }

    @Test
    fun validSearchWithWhiteSpace() = runTest(dispatcher) {
        val queryWithTrailingWhitespace = "   $VALID_QUERY       "
        testSearch(queryWithTrailingWhitespace, 5)
    }

    private suspend fun testSearch(query: String, expectedResultsSize: Int)  {
        val song1 = mock<MediaItem>()
        val song2 = mock<MediaItem>()
        val songs: MutableList<MediaItem> = ArrayList()
        songs.add(song1)
        songs.add(song2)
        val songSearcher = getContentSearch(MediaItemType.SONGS, songs)
        val folder1 = mock<MediaItem>()
        val folders: MutableList<MediaItem> = ArrayList()
        folders.add(folder1)
        val folderSearcher = getContentSearch(MediaItemType.FOLDER, folders)
        val contentSearcherList: MutableList<ContentSearcher> = ArrayList()
        contentSearcherList.add(songSearcher)
        contentSearcherList.add(folderSearcher)
        whenever(contentSearchers.all).thenReturn(contentSearcherList)
        whenever(contentSearchers[songSearcher.searchCategory]).thenReturn(songSearcher)
        whenever(contentSearchers[folderSearcher.searchCategory]).thenReturn(folderSearcher)
        val result = contentManager!!.search(query)
        Assert.assertNotNull(result)
        val resultSize = result.size
        Assert.assertEquals(expectedResultsSize.toLong(), resultSize.toLong())
    }

    private suspend fun getContentSearch(type: MediaItemType, result: List<MediaItem>): ContentSearcher {
        val contentSearcher = mock<ContentSearcher>()
        whenever<Any?>(contentSearcher.searchCategory).thenReturn(type)
        whenever(contentSearcher.search(VALID_QUERY)).thenReturn(result.toMutableList())
        return contentSearcher
    }

    companion object {
        private const val VALID_QUERY = "QUERY"
        private const val LOWER_CASE_VALID_QUERY = "query"
        private val contentSearcherMap: Map<MediaItemType, ContentSearcher>? = null
    }
}