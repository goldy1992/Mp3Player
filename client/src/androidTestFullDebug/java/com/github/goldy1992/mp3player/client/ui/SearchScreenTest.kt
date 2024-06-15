package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.SearchResult
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.components.SharedElementComposable
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreen
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for [SearchScreen].
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterial3WindowSizeClassApi
@ExperimentalTestApi
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
    @Test
    fun testSearchBarOnValueChange() {
        composeTestRule.setContent {
            SharedElementComposable { animatedVisibilityScope ->
                SearchScreen(
                    animatedContentScope = animatedVisibilityScope,
                    viewModel = searchScreenViewModel,
                    //  windowSize = WindowSizeClass.calculateFromSize(WindowWidthSizeClass.Compact)
                )
            }
        }
        val searchTextFieldName = context.resources.getString(R.string.search_text_field)

        composeTestRule.onNodeWithContentDescription(searchTextFieldName).performTextReplacement("ab")
        composeTestRule.waitUntilExactlyOneExists(hasText("ab"))
        composeTestRule.onNodeWithContentDescription(searchTextFieldName).assert(hasText("ab"))
    }

    /**
     * Tests the song is played when selected from the results.
     */
    @Test
    fun testSearchResultsDisplayedCorrectly() {
        val searchQuery = "query"
        val expectedLibId= "sdfsdf"
        val songTitle = "songTitle"
        val songDuration = 10000L
        val song = Song(id = expectedLibId, title = songTitle, duration = songDuration)
        val songSearchResult = SearchResult(id = searchQuery, type = MediaItemType.SONG, value = song)

        val folderName = "/c/folder1"
        val folder = Folder(id = "a", name = folderName)
        val folderSearchResult = SearchResult(id = searchQuery, type = MediaItemType.FOLDER, value = folder)

        testMediaRepository.searchResults = SearchResults(
            State.LOADED,  listOf(songSearchResult, folderSearchResult))

        // push a change of state of change to search results
        testMediaRepository.searchResultsChangedState.value = SearchResultsChangedEvent(searchQuery, 2)
        val searchResultsColumn = context.resources.getString(R.string.search_results_column)

        composeTestRule.setContent {
            SharedElementComposable { animatedVisibilityScope ->
                SearchScreen(
                    animatedContentScope = animatedVisibilityScope,
                    viewModel = searchScreenViewModel,
                )
            }
        }
        val assertText = { text: String ->
            composeTestRule.onNodeWithText(text).assertExists(
                "Could not file $text in semantic tree.")
        }
        assertText(songTitle)
        assertText(folderName)
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
            SharedElementComposable { animatedVisibilityScope ->
                SearchScreen(
                    animatedContentScope = animatedVisibilityScope,
                    viewModel = searchScreenViewModel,
                )
            }
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