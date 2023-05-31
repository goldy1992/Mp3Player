package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock

/**
 * Test class for the [MainScreen] composable function.
 */

@RunWith(AndroidJUnit4::class)
class LibraryScreenTest {

    private val testMediaRepository = TestMediaRepository()

//    val onChildrenChangedFlowObj = mock<OnChildrenChangedFlow>()
//    val onChildrenChangedFlow = MutableStateFlow(
//                                    OnChildrenChangedEventHolder(mockMediaBrowser,
//                                        "",
//                                        1,
//                                        MediaLibraryService.LibraryParams.Builder().build())
//    )

    @get:Rule
    val composeTestRule = createComposeRule()


    private val navController = mock<NavController>()

    lateinit var context : Context

    private lateinit var libraryScreenViewModel: LibraryScreenViewModel

    companion object{
        private const val TEST_ROOT_ID = "rootId"
        private val testRootItem =  MediaItem.Builder().setMediaId(TEST_ROOT_ID).build()
    }

    /**
     * Setup method.
     */
    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.testMediaRepository.libraryRootState = testRootItem
        this.libraryScreenViewModel = LibraryScreenViewModel(
            mediaRepository = testMediaRepository
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Test
    fun testDisplay() {
        val songsId = "SongsId"
        val songExtras = Bundle()
        songExtras.putSerializable(Constants.ROOT_ITEM_TYPE, MediaItemType.SONGS)
        songExtras.putSerializable(Constants.MEDIA_ITEM_TYPE, MediaItemType.SONGS)
        val songsItem = MediaItem.Builder().setMediaId(songsId).setMediaMetadata(
            MediaMetadata.Builder()
                .setExtras(songExtras).build()
        )
        .build()

        testMediaRepository.getChildrenState = listOf(songsItem)
        testMediaRepository.onChildrenChangedState.value = OnChildrenChangedEventHolder(TEST_ROOT_ID, itemCount = 1, getDefaultLibraryParams())

        composeTestRule.setContent {
            LibraryScreen(
                navController = navController,
                viewModel = libraryScreenViewModel
            )
        }

        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onAllNodesWithText(MediaItemType.SONGS.name).fetchSemanticsNodes()
                .isNotEmpty()
        }

    }


}