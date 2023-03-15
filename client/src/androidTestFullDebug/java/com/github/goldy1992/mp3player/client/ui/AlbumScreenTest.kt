package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.util.Base64
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.screens.album.AlbumScreen
import com.github.goldy1992.mp3player.client.ui.screens.album.AlbumScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy

/**
 * Test class for [AlbumScreen].
 */
class AlbumScreenTest {

    private lateinit var context : Context
    private val mockNavController = mock<NavController>()
    private lateinit var albumScreenViewModel: AlbumScreenViewModel

    private val testMediaRepository = TestMediaRepository()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        this.context = InstrumentationRegistry.getInstrumentation().context
        this.testMediaRepository.currentSearchQuery.value = "query"

    }

    @Test
    fun testDisplayAlbumScreen() {
        val savedStateHandle = SavedStateHandle()
        val expectedAlbumArtist = "Album artist"
        val expectedAlbumTitle = "Album title"
        val albumId = "id"
        val albumUri = "content://album1"
        val albumUriEncoded = Base64.encodeToString(albumUri.toByteArray(), Base64.DEFAULT)
        savedStateHandle["albumId"] = albumId
        savedStateHandle["albumTitle"] = expectedAlbumTitle
        savedStateHandle["albumArtist"] = expectedAlbumArtist
        savedStateHandle["albumArtUri"] = albumUriEncoded

        this.albumScreenViewModel = spy(
            AlbumScreenViewModel(
                mediaRepository = testMediaRepository,
                savedStateHandle = savedStateHandle
            )
        )

        val expectedAlbumSongs = mutableListOf<MediaItem>()
        val expectedSong1Title = "song1Title"
        val song1 = MediaItemBuilder("id1")
            .setTitle(expectedSong1Title)
            .setDuration(3000L)
            .setArtist("artist1")
            .build()
        val expectedSong2Title = "song2Title"
        val song2 = MediaItemBuilder("id2")
            .setTitle(expectedSong2Title)
            .setDuration(6000L)
            .setArtist("artist2")
            .build()
        expectedAlbumSongs.add(song1)
        expectedAlbumSongs.add(song2)
        testMediaRepository.getChildrenState = expectedAlbumSongs

        composeTestRule.setContent {
            AlbumScreen(
                viewModel = albumScreenViewModel,
                navController = mockNavController,
                windowSize = WindowSize.Compact
            )
        }

        composeTestRule.onNodeWithText(expectedAlbumTitle).assertExists()
        runBlocking { composeTestRule.awaitIdle() }
        composeTestRule.onNodeWithText(expectedSong1Title).assertExists()
        composeTestRule.onNodeWithText(expectedSong2Title).assertExists()

    }

}