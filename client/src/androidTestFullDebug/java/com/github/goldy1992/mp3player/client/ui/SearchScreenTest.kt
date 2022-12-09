package com.github.goldy1992.mp3player.client.ui

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.*
import java.io.File

/**
 * Test class for [SearchScreen].
 */
@OptIn(ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class)
class SearchScreenTest : MediaTestBase(){


    private lateinit var searchScreenViewModel: SearchScreenViewModel

    private val searchResultsChangedFlow = MutableStateFlow(
        OnSearchResultsChangedEventHolder(
        mockMediaBrowser,
        "",
        1,
        MediaLibraryService.LibraryParams.Builder().build()

    )
    )
    private val searchResultsChangedFlowObj = mock<OnSearchResultsChangedFlow>()

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalComposeUiApi::class,
        ExperimentalFoundationApi::class)
    @Before
    override fun setup() {
        val scope : CoroutineScope
        val mainDispatcher = Dispatchers.Main
        runBlocking {
            scope = this
        }
        super.setup(scope, mainDispatcher)
        whenever(searchResultsChangedFlowObj.flow).thenReturn(searchResultsChangedFlow)
        whenever(mockMediaBrowser.getSearchResult(any(), any(), any(), any()))
            .thenReturn(
                Futures.immediateFuture(
                    LibraryResult.ofItemList(
                        ImmutableList.of(),
                        MediaLibraryService.LibraryParams.Builder().build())))
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.searchScreenViewModel = SearchScreenViewModel(
            mediaBrowserAdapter = mediaBrowserAdapter,
            mediaControllerAdapter = mediaControllerAdapter,
            onSearchResultsChangedFlow = this.searchResultsChangedFlowObj,
            isPlayingFlow = isPlayingFlowObj,
            mainDispatcher = Dispatchers.Main
        )
    }

    @Test
    fun testSearchBarOnValueChange() = runTest {

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
        val searchTextFieldName = context.resources.getString(R.string.search_text_field)
        val captor : ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        runBlocking {
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("b")
            composeTestRule.awaitIdle()
        }
        verify(mockMediaBrowser, times(2)).search(captor.capture(), anyOrNull())
        assertEquals("a", captor.allValues[0])
        assertEquals("ab", captor.allValues[1])
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Test
    fun testSearchResultsPlaySong() = runTest {

        val expectedLibId= "sdfsdf"
        val songTitle = "songTitle"
        val songItem = MediaItemBuilder("a")
            .setLibraryId(expectedLibId)
            .setMediaItemType(MediaItemType.SONG)
            .setDuration(10000L)
            .setTitle(songTitle)
            .build()

        whenever(mockMediaBrowser.getSearchResult(any(), any(), any(), any()))
            .thenReturn(
                Futures.immediateFuture(
                    LibraryResult.ofItemList(
                        ImmutableList.of(songItem),
                        MediaLibraryService.LibraryParams.Builder().build())))

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText(songTitle).performClick()
            composeTestRule.awaitIdle()
       //     verify(mockMediaController, times(1)).playFromMediaId(expectedLibId, null)
        }
    }


    @Test
    fun testSearchResultsOpenFolder() = runTest {

        val folderName = "/c/folder1"
        val libId = "3fk4"

        val folderItem = MediaItemBuilder("a")
            .setMediaItemType(MediaItemType.FOLDER)
            .setTitle(folderName)
            .setLibraryId(libId)
            .setDirectoryFile(File(folderName))
            .build()
        val folderLibraryId = MediaItemUtils.getLibraryId(folderItem)
        val encodedFolderLibraryId = Uri.encode(folderLibraryId)
        val directoryPath = MediaItemUtils.getDirectoryPath(folderItem)
        val encodedFolderPath = Uri.encode(directoryPath)
        val folderNameMi = MediaItemUtils.getDirectoryName(folderItem)

       val expectedRoute = Screen.FOLDER.name + "/" + encodedFolderLibraryId+ "/" + folderNameMi+ "/" + encodedFolderPath
        val expectedResult = Futures.immediateFuture(LibraryResult.ofItemList(mutableListOf(folderItem), MediaLibraryService.LibraryParams.Builder().build()))
        whenever(mockMediaBrowser.getSearchResult(any(), any(), any(), any())).thenReturn(expectedResult)

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText(folderName).performClick()
            composeTestRule.awaitIdle()
            verify(mockNavController, times(1)).navigate(expectedRoute)
        }
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
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription(clearSearchButton).assertDoesNotExist()
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