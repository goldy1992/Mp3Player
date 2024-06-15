package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.models.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.components.SharedElementComposable
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.EnumMap

/**
 * Test class for the [MainScreen] composable function.
 */
@RunWith(AndroidJUnit4::class)
class LibraryScreenTest {

    private val testMediaRepository = TestMediaRepository()

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context : Context

    private lateinit var libraryScreenViewModel: LibraryScreenViewModel

    companion object{
        private const val TEST_ROOT_ID = "rootId"
        private val testRootItem = Root(id = TEST_ROOT_ID)
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
        val songs = Playlist(id = songsId)
        val originalRoot = testMediaRepository.libraryRootState
        val loadedRoot = Root(id = originalRoot.id, state = State.LOADED, childMap = EnumMap(
            mapOf<MediaItemType, MediaEntity>(MediaItemType.SONGS to songs)))

        testMediaRepository.getChildrenState = loadedRoot
        testMediaRepository.onChildrenChangedState.value = ChildrenChangedEvent(TEST_ROOT_ID, itemCount = 1, Bundle())

        composeTestRule.setContent {
            SharedElementComposable { animatedVisibilityScope ->

                LibraryScreen(
                    viewModel = libraryScreenViewModel,
                    animatedContentScope = animatedVisibilityScope
                )
            }
        }

        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onAllNodesWithText(MediaItemType.SONGS.name).fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}