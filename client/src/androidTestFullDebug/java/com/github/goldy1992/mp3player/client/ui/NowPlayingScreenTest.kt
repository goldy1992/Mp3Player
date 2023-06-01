package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.screens.nowplaying.NowPlayingScreen
import com.github.goldy1992.mp3player.client.ui.screens.nowplaying.NowPlayingScreenViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy

@OptIn(InternalCoroutinesApi::class)
class NowPlayingScreenTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private val mockNavController = mock<NavController>()

    private val testMediaRepository = TestMediaRepository()

    private lateinit var nowPlayingScreenViewModel: NowPlayingScreenViewModel

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        this.nowPlayingScreenViewModel = spy(
            NowPlayingScreenViewModel(
                mediaRepository = testMediaRepository)
            )
    }


    @Test
    fun testDisplay() {
        val expectedTitle = "MySong"
        testMediaRepository.metadataState.value = MediaMetadata.Builder()
            .setTitle(expectedTitle)
            .build()
        composeTestRule.setContent {
            NowPlayingScreen(navController = mockNavController,
            viewModel = nowPlayingScreenViewModel)
        }
        val titleNode = context.resources.getString(R.string.song_title)
        composeTestRule.onNodeWithContentDescription(titleNode).assert(hasText(expectedTitle))
    }
}