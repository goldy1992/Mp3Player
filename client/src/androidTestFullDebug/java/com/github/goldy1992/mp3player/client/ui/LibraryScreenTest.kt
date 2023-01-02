package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallLibraryAppBar
import com.github.goldy1992.mp3player.client.ui.screens.library.SmallLibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

/**
 * Test class for the [MainScreen] composable function.
 */
@OptIn(ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class)
@HiltAndroidTest
class LibraryScreenTest {


    private val testMediaRepository = TestMediaRepository()

//    val onChildrenChangedFlowObj = mock<OnChildrenChangedFlow>()
//    val onChildrenChangedFlow = MutableStateFlow(
//                                    OnChildrenChangedEventHolder(mockMediaBrowser,
//                                        "",
//                                        1,
//                                        MediaLibraryService.LibraryParams.Builder().build())
//    )


    @Mock
    val mainDispatcher : CoroutineDispatcher = mock<CoroutineDispatcher>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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

    @Test
    fun testDisplay() {

        composeTestRule.setContent {
            LibraryScreen(
                navController = navController,
                viewModel = libraryScreenViewModel
            )
        }
        val songsId = "SongsId"
        val songExtras = Bundle()
        songExtras.putSerializable(Constants.ROOT_ITEM_TYPE, MediaItemType.SONGS)
        val songsItem = MediaItem.Builder().setMediaId(songsId).setMediaMetadata(
            MediaMetadata.Builder().setExtras(songExtras).build()
        )
            .build()
        testMediaRepository.getChildrenState = listOf(songsItem)
        testMediaRepository.onChildrenChangedState.value = OnChildrenChangedEventHolder(TEST_ROOT_ID, itemCount = 1, getDefaultLibraryParams())


        composeTestRule.waitUntil(5000L) {
            composeTestRule.onAllNodesWithText(MediaItemType.SONGS.name).fetchSemanticsNodes()
                .isNotEmpty()
        }

        assertTrue(true)
    }


}