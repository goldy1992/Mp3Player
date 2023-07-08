package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.SearchResult
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreen
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

/**
 * Test class for [SearchScreen].
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class SearchScreenTest {

    private lateinit var context : Context
    private lateinit var searchScreenViewModel: SearchScreenViewModel
    private val testMediaRepository = TestMediaRepository()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.testMediaRepository.currentSearchQuery.value = "query"
        this.searchScreenViewModel = SearchScreenViewModel(
            mediaRepository = testMediaRepository
        )
    }

    /**
     * Tests that the query is updated correctly when we perform text input.
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testSearchBarOnValueChange() {
        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                windowSize = WindowSize.Compact
            )
        }
        val searchTextFieldName = context.resources.getString(R.string.search_text_field)

        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextReplacement("ab")
        composeTestRule.waitUntilExactlyOneExists(hasText("ab"))
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).assert(hasText("ab"))
    }

    /**
     * Tests the song is played when selected from the results.
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Test
    fun testSearchResultsDisplayedCorrectly() {
        val searchQuery = "query"
        val expectedLibId= "sdfsdf"
        val songTitle = "songTitle"
        val songDuration = 10000L
        val song = Song(id = expectedLibId, title = songTitle, duration = songDuration)
        val songSearchResult = SearchResult(id = searchQuery, type = MediaItemType.SONG, value = song)

        val folderName = "/c/folder1"
        val libId = "3fk4"


        val folderItem = MediaItemBuilder("a")
            .setMediaItemType(MediaItemType.FOLDER)
            .setTitle(folderName)
            .setLibraryId(libId)
            .setDirectoryFile(File(folderName))
            .build()

        val folder = Folder(id = "a", name = folderName)
        val folderSearchResult = SearchResult(id = searchQuery, type = MediaItemType.FOLDER, value = folder)

        testMediaRepository.searchResults = SearchResults(
            State.LOADED,  listOf(songSearchResult, folderSearchResult))

        // push a change of state of change to search results
        testMediaRepository.searchResultsChangedState.value = SearchResultsChangedEvent(searchQuery, 2)
        val searchResultsColumn = context.resources.getString(R.string.search_results_column)

        composeTestRule.setContent {
            SearchScreen(
                viewModel = searchScreenViewModel,
                windowSize = WindowSize.Compact
            )
        }

        composeTestRule.onNodeWithContentDescription(searchResultsColumn)
            .onChildAt(0)
            .onChildren()
            .assertAny(hasText(songTitle)
        )

        composeTestRule.onNodeWithContentDescription(searchResultsColumn)
            .onChildAt(1)
            .onChildren()
            .assertAny(hasText(folderName))
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