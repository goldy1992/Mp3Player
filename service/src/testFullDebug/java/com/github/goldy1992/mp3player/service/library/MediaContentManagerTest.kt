package com.github.goldy1992.mp3player.service.library

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import com.github.goldy1992.mp3player.service.RootAuthenticator
import com.github.goldy1992.mp3player.service.library.content.retrievers.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.retrievers.ContentRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MediaContentManagerTest {
    private var contentManager: ContentManager? = null

    private var permissionsRepository : IPermissionsRepository = mock()

    private val rootAuthenticator = mock<RootAuthenticator>()

    private val contentRetrievers: ContentRetrievers = mock<ContentRetrievers>()

    private val contentSearchers: ContentSearchers = mock()
    
    private val rootRetriever: RootRetriever = mock<RootRetriever>()

    private val mockContentRetriever = mock<ContentRetriever>()
    
    private val rootItem: MediaItem = mock<MediaItem>()


    private val testScheduler = TestCoroutineScheduler()
    private val dispatcher  = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(dispatcher)

    private val testRootId = "rootId"
    private val testRootItem = MediaItemBuilder(testRootId)
        .setMediaItemType(MediaItemType.ROOT)
        .build()

    private val testSongsId = "songsId"
    private val testSongsMediaItem = MediaItemBuilder(testSongsId)
        .setMediaItemType(MediaItemType.SONGS)
        .build()

    private val testSongsChildId = "songsChildId"
    private val testSongsChildMediaItem = MediaItemBuilder(testSongsChildId)
        .setMediaItemType(MediaItemType.SONGS)
        .build()

    @Before
    fun setup() {
        whenever(rootAuthenticator.getRootItem()).thenReturn(testRootItem)
        whenever(contentRetrievers.getContentRetriever(MediaItemType.ROOT)).thenReturn(rootRetriever)
        whenever(rootRetriever.getChildren(testRootId)).thenReturn(listOf(testSongsMediaItem))
        whenever(rootRetriever.getRootItem(MediaItemType.SONGS)).thenReturn(testSongsMediaItem)
        whenever(contentRetrievers.rootRetriever()).thenReturn(rootRetriever)
        whenever(mockContentRetriever.getChildren(testSongsId)).thenReturn(listOf(testSongsChildMediaItem))

        contentManager = MediaContentManager(
                permissionsRepository,
                contentRetrievers,
                contentSearchers,
                rootAuthenticator,
                testScope)
    }


    /**
     * Note: run test is a different scope to testScope so that testScope can be cancelled.
     */
    @Test
    fun testGetChildrenUsingRootId() = runTest  {
         testScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            val result = contentManager!!.getChildren(testRootId)
            assertEquals(1, result.children.size)
            assertEquals(testSongsMediaItem, result.children[0])
        }
        testScope.cancel()
    }

    /**
     * Note: run test is a different scope to testScope so that testScope can be cancelled.
     */
    @Test
    fun testGetChildrenUsingSongsIdBeforePermissionGranted() = runTest  {
        testScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            val result = contentManager!!.getChildren(testSongsId)
            assertEquals(0, result.children.size)
        }
        testScope.cancel()
    }

    /**
     * Note: run test is a different scope to testScope so that testScope can be cancelled.
     */
    @Test
    fun testGetChildrenUsingSongsIdAfterPermissionGranted() = runTest  {
        //permissionsNotifier.setPermissionGranted(true)
        testScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            val result = contentManager!!.getChildren(testSongsId)
            assertEquals(1, result.children.size)
            assertEquals(testSongsChildMediaItem, result.children.get(0))
        }
        testScope.cancel()
    }

    @Test
    fun testGetChildrenIncorrectIdReturnsEmptyList() {
        val incorrectId = "incorrectId"
       // val contentRequest = ContentRequest("", incorrectId, null)
    //    whenever(contentRequestParser.parse(incorrectId)).thenReturn(contentRequest)
   //     val result = contentManager!!.getChildren(incorrectId)
      //  Assert.assertTrue(result.isEmpty())
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