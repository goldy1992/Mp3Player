package com.github.goldy1992.mp3player.client.ui

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.io.File

/**
 * Test class for [SearchScreen].
 */
class SearchScreenTest : MediaTestBase(){

    private val mockMediaRepo : MediaRepository = MediaRepository(MutableLiveData())

    @get:Rule
    val composeTestRule = createComposeRule()

    @kotlin.OptIn(ExperimentalComposeUiApi::class,
        ExperimentalFoundationApi::class)
    @Before
    override fun setup() {
        super.setup()
        this.context = InstrumentationRegistry.getInstrumentation().context
        composeTestRule.setContent {
            SearchScreen(
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }
    }

    @Test
    fun testSearchBarOnValueChange() = runTest {
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

    @Test
    fun testSearchResultsPlaySong() {
        val expectedLibId= "sdfsdf"
        val songTitle = "songTitle"
        val songItem = MediaItemBuilder("a")
            .setLibraryId(expectedLibId)
            .setMediaItemType(MediaItemType.SONG)
            .setTitle(songTitle)
            .build()
      //  searchResultsState.postValue(mutableListOf(songItem))

        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText(songTitle).performClick()
            composeTestRule.awaitIdle()
       //     verify(mockMediaController, times(1)).playFromMediaId(expectedLibId, null)
        }
    }


    @Test
    fun testSearchResultsOpenFolder() {
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
  //      searchResultsState.postValue(mutableListOf(folderItem))

        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText(folderName).performClick()
            composeTestRule.awaitIdle()
            verify(mockNavController, times(1)).navigate(expectedRoute)
        }
    }

    @Test
    fun testClearSearch() {
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