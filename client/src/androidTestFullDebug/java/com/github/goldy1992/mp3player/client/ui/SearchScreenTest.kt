package com.github.goldy1992.mp3player.client.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.MediaTestUtils
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.audiobands.media.browser.MediaBrowserRepository
import com.github.goldy1992.mp3player.client.data.audiobands.media.controller.PlaybackStateRepository
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreen
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.kotlin.*
import java.io.File

/**
 * Test class for [SearchScreen].
 */
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalFoundationApi::class)
class SearchScreenTest : MediaTestBase(){

    private lateinit var searchScreenViewModel: SearchScreenViewModel

    private val browserRepository = TestMediaBrowserRepository()
    private val playbackStateRepository = TestPlaybackStateRepository()

    private val defaultSearchResultEventHolder =
        OnSearchResultsChangedEventHolder(
            browser = mockMediaBrowser,
            query = "query",
            itemCount = 1,
            params = MediaLibraryService.LibraryParams.Builder().build()
        )

    private val searchResultsChangedFlow = MutableStateFlow(defaultSearchResultEventHolder)

    private val searchResultsChangedFlowObj = mock<OnSearchResultsChangedFlow>()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    override fun setup() {
        super.setup()
        whenever(searchResultsChangedFlowObj.flow).thenReturn(searchResultsChangedFlow)
        whenever(mockMediaBrowser.getSearchResult(any(), any(), any(), any()))
            .thenReturn(
                Futures.immediateFuture(
                    LibraryResult.ofItemList(
                        ImmutableList.of(),
                        MediaLibraryService.LibraryParams.Builder().build())))
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.searchScreenViewModel = spy(SearchScreenViewModel(
            browserRepository = browserRepository,
            playbackStateRepository = playbackStateRepository
        ))
        doAnswer {
            Log.i("SearchScreenTest", "hit do answer")
            searchResultsChangedFlow.value = OnSearchResultsChangedEventHolder(
                browser = mockMediaBrowser,
                query = it.arguments[0] as String,
                itemCount = 1,
                params = MediaLibraryService.LibraryParams.Builder().build()
            )
            Futures.immediateFuture(LibraryResult.ofVoid())
        }.whenever(this.mockMediaBrowser).search(anyString(), any())
        reset(mockMediaBrowser)
    }

    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    @Test
    fun testSearchBarOnValueChange() {


        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
        val searchTextFieldName = context.resources.getString(R.string.search_text_field)
        val captor : ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        Log.i("SScrnTest", "captor: ${captor}")
        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("b")
            composeTestRule.awaitIdle()
        }
        verify(searchScreenViewModel, atLeastOnce()).setSearchQuery(capture(captor))
//        verify(searchScreenViewModel, atLeastOnce()).setSearchQuery("b")
        Log.i("SearchScreen", "captor.all Values ${captor.allValues}")

        var hasA = false
        var hasB = false
        captor.allValues.forEach {
            if (it.contains("a")) {
                hasA = true
            }
            if (it.contains("b")) {
                hasB = true
            }
        }
        assertTrue(hasA)
        assertTrue(hasB)

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Test
    fun testSearchResultsPlaySong() {
        val expectedLibId= "sdfsdf"
        val songTitle = "songTitle"
        val songItem = MediaItemBuilder("a")
            .setLibraryId(expectedLibId)
            .setMediaItemType(MediaItemType.SONG)
            .setDuration(10000L)
            .setTitle(songTitle)
            .build()

        browserRepository.currentSearchResults.value = listOf(songItem)

        val searchTextFieldName = context.resources.getString(R.string.search_text_field)
        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }

        composeTestRule.onNodeWithText(songTitle).performClick()
        verify(searchScreenViewModel, times(1)).play(songItem)
    }


    @Test
    fun testSearchResultsOpenFolder()  {

        val folderName = "/c/folder1"
        val libId = "3fk4"

        val folderItem = MediaItemBuilder("a")
            .setMediaItemType(MediaItemType.FOLDER)
            .setTitle(folderName)
            .setLibraryId(libId)
            .setDirectoryFile(File(folderName))
            .build()

        val directoryPath = MediaItemUtils.getDirectoryPath(folderItem)
        val encodedFolderPath = Uri.encode(directoryPath)
        val folderNameMi = MediaItemUtils.getDirectoryName(folderItem)

        val expectedRoute = Screen.FOLDER.name + "/" + folderItem.mediaId+ "/" + folderNameMi+ "/" + encodedFolderPath
        browserRepository.currentSearchResults.value = listOf(folderItem)

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact,
            )
        }
        runBlocking {
            composeTestRule.onNodeWithText(folderName).performClick()
            composeTestRule.awaitIdle()
        }
        verify(mockNavController, times(1)).navigate(expectedRoute)

    }

    @Test
    fun testClearSearch() = runTest {

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
        val clearSearchButton = context.resources.getString(R.string.clear_search)
        val searchTextFieldName = context.resources.getString(R.string.search_text_field)
        runBlocking {
            searchScreenViewModel.setSearchQuery("")
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(clearSearchButton).assertExists()
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(clearSearchButton).assertExists()
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextReplacement("")
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(clearSearchButton).assertDoesNotExist()
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(clearSearchButton).assertExists().performClick()
            composeTestRule.awaitIdle()
      //      verify(mockMediaBrowser, times(1)).clearSearchResults()
            composeTestRule.onNodeWithContentDescription(clearSearchButton).assertDoesNotExist()
        }
    }

}