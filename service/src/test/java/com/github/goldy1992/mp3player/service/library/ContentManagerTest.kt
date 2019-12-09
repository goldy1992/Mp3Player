package com.github.goldy1992.mp3player.service.library

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequestParser
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.MediaItemFromIdRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class ContentManagerTest {
    private var contentManager: ContentManager? = null
    @Mock
    private val contentRequestParser: ContentRequestParser? = null
    @Mock
    private val contentRetrievers: ContentRetrievers? = null
    @Mock
    private val contentSearchers: ContentSearchers? = null
    @Mock
    private val rootRetriever: RootRetriever? = null
    @Mock
    private val rootItem: MediaBrowserCompat.MediaItem? = null
    @Mock
    private val mediaItemFromIdRetriever: MediaItemFromIdRetriever? = null
    @Mock
    private val songFromUriRetriever: SongFromUriRetriever? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(contentRetrievers!!.root).thenReturn(rootRetriever)
        Mockito.`when`(rootRetriever!!.getRootItem(ArgumentMatchers.any<MediaItemType>())).thenReturn(rootItem)
        contentManager = ContentManager(contentRetrievers,
                contentSearchers!!,
                contentRequestParser!!,
                songFromUriRetriever!!,
                mediaItemFromIdRetriever!!)
    }

    @Test
    fun testGetChildren() {
        val contentRetrieverId = "id"
        val expectedList: List<MediaBrowserCompat.MediaItem> = ArrayList()
        val contentRetriever = Mockito.mock(ContentRetriever::class.java)
        Mockito.`when`(contentRetrievers!![contentRetrieverId]).thenReturn(contentRetriever)
        val contentRequest = ContentRequest("", contentRetrieverId, null)
        Mockito.`when`(contentRequestParser!!.parse(contentRetrieverId)).thenReturn(contentRequest)
        Mockito.`when`(contentRetriever.getChildren(contentRequest)).thenReturn(expectedList)
        val result = contentManager!!.getChildren(contentRetrieverId)
        Assert.assertEquals(expectedList, result)
    }

    @Test
    fun testGetChildrenNull() {
        val incorrectId = "incorrectId"
        val contentRequest = ContentRequest("", incorrectId, null)
        Mockito.`when`(contentRequestParser!!.parse(incorrectId)).thenReturn(contentRequest)
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
    fun testValidSearchQuery() {
        testSearch(LOWER_CASE_VALID_QUERY, 5)
    }

    @Test
    fun ValidSearchWithWhiteSpace() {
        val queryWithTrailingWhitespace = "   $VALID_QUERY       "
        testSearch(queryWithTrailingWhitespace, 5)
    }

    private fun testSearch(query: String, expectedResultsSize: Int) {
        val song1 = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
        val song2 = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
        val songs: MutableList<MediaBrowserCompat.MediaItem> = ArrayList()
        songs.add(song1)
        songs.add(song2)
        val songSearcher = getContentSearch(MediaItemType.SONGS, songs)
        val folder1 = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
        val folders: MutableList<MediaBrowserCompat.MediaItem> = ArrayList()
        folders.add(folder1)
        val folderSearcher = getContentSearch(MediaItemType.FOLDER, folders)
        val contentSearcherList: MutableList<ContentSearcher> = ArrayList()
        contentSearcherList.add(songSearcher)
        contentSearcherList.add(folderSearcher)
        Mockito.`when`(contentSearchers!!.all).thenReturn(contentSearcherList)
        Mockito.`when`(contentSearchers[songSearcher.searchCategory]).thenReturn(songSearcher)
        Mockito.`when`(contentSearchers[folderSearcher.searchCategory]).thenReturn(folderSearcher)
        val result = contentManager!!.search(query)
        Assert.assertNotNull(result)
        val resultSize = result.size
        Assert.assertEquals(expectedResultsSize.toLong(), resultSize.toLong())
    }

    private fun getContentSearch(type: MediaItemType, result: List<MediaBrowserCompat.MediaItem>): ContentSearcher {
        val contentSearcher = Mockito.mock(ContentSearcher::class.java)
        Mockito.`when`<Any?>(contentSearcher.searchCategory).thenReturn(type)
        Mockito.`when`(contentSearcher.search(VALID_QUERY)).thenReturn(result.toMutableList())
        return contentSearcher
    }

    companion object {
        private const val VALID_QUERY = "QUERY"
        private const val LOWER_CASE_VALID_QUERY = "query"
        private val contentSearcherMap: Map<MediaItemType, ContentSearcher>? = null
    }
}