package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreen
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.*
import java.io.File

/**
 * Test class for [SearchScreen].
 */
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class)
class SearchScreenTest {

    private lateinit var context : Context
    private val mockNavController = mock<NavController>()
    private lateinit var searchScreenViewModel: SearchScreenViewModel

    private val testMediaRepository = TestMediaRepository()


    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.testMediaRepository.currentSearchQuery.value = "query"
        this.searchScreenViewModel = spy(SearchScreenViewModel(
            mediaRepository = testMediaRepository
        ))
    }


    /**
     * Tests that the query is updated correctly when we perform text input.
     */
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
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("b")
        verify(searchScreenViewModel, atLeastOnce()).setSearchQuery(capture(captor))
        Log.i("SearchScreen", "captor.all Values ${captor.allValues}")

        /* Composes test performInput seems to input the query in a different combination every time.
            To combat this we just assert that "a" is input at least once and "b" is also input at
            least once.
         */
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

    /**
     * Tests the song is played when selected from the results.
     */
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
        val expectedSong = Song(
            id="a",
            duration = 10000L,
            title = songTitle
        )

        testMediaRepository.searchResults = listOf(songItem)
        // push a change of state of change to search results
        testMediaRepository.searchResultsChangedState.value = OnSearchResultsChangedEventHolder("newQuery", 1)


        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }

        composeTestRule.onNodeWithText(songTitle).performClick()
        verify(searchScreenViewModel, times(1)).play(expectedSong)
    }

    /**
     * Tests the navigation is set up correctly when a folder is clicked.
     */
    @Test
    fun testSearchResultsOpenFolder()  {
        val captor : ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        val folderName = "/c/folder1"
        val libId = "3fk4"

        val folderItem = MediaItemBuilder("a")
            .setMediaItemType(MediaItemType.FOLDER)
            .setTitle(folderName)
            .setLibraryId(libId)
            .setDirectoryFile(File(folderName))
            .build()

        val directoryPath = MediaItemUtils.getDirectoryPath(folderItem)
        val encodedFolderPath = "file://$directoryPath"
        val folderNameMi = MediaItemUtils.getDirectoryName(folderItem)

        val expectedRoute = Screen.FOLDER.name + "/" + folderItem.mediaId+ "/" + folderNameMi+ "/" + encodedFolderPath
        testMediaRepository.searchResults = listOf(folderItem)
        // push a change of state of change to search results
        testMediaRepository.searchResultsChangedState.value = OnSearchResultsChangedEventHolder("newQuery", 1)

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact,
            )
        }
        composeTestRule.onNodeWithText(folderName, useUnmergedTree = true).performClick()
        Log.i("SearchScreenTest", "expectedRoute : $expectedRoute")


        verify(mockNavController, times(1)).navigate(capture(captor),eq(null), eq(null))

        val callArgs = captor.value
        assertNotNull(callArgs)
        assertEquals(expectedRoute, captor.value)

    }

    /**
     * Tests the following use cases
     * - the clear search button is present when the query has text
     * - the clear search button is NOT present when the query has NO test
     * - the clear search button disappears when clicked and the query is set to empty
     */
    @Test
    fun testClearSearch() {
        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
        val clearSearchButton = context.resources.getString(R.string.clear_search)
        val searchTextFieldName = context.resources.getString(R.string.search_text_field)
        searchScreenViewModel.setSearchQuery("query")
        composeTestRule.onNodeWithContentDescription(clearSearchButton).assertExists()
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
        composeTestRule.onNodeWithContentDescription(clearSearchButton).assertExists()
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextReplacement("")
        composeTestRule.onNodeWithContentDescription(clearSearchButton).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextInput("a")
        composeTestRule.onNodeWithContentDescription(clearSearchButton).assertExists().performClick()
        composeTestRule.onNodeWithContentDescription(clearSearchButton).assertDoesNotExist()
    }
}