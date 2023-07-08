package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.util.Base64
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import com.github.goldy1992.mp3player.client.ui.screens.album.AlbumScreen
import com.github.goldy1992.mp3player.client.ui.screens.album.AlbumScreenViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test class for [AlbumScreen].
 */
class AlbumScreenTest {

    private lateinit var context : Context
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

        val expectedSong1Title = "song1Title"
        val expectedSong1Artist = "song1Artist"
        val expectedSong1Duration = 3000L
        val song1 = Song(id = "id1", title = expectedSong1Title, duration = expectedSong1Duration, artist = expectedSong1Artist )
        val expectedSong2Title = "song2Title"
        val expectedSong2Artist = "song2Artist"
        val expectedSong2Duration = 6000L
        val song2 = Song(id = "id2", title = expectedSong2Title, duration = expectedSong2Duration, artist = expectedSong2Artist )

        val albumSongs = listOf(song1, song2)
        val albumPlaylist = Playlist(id = albumId, state = State.LOADED, songs = albumSongs)
        testMediaRepository.playlist = albumPlaylist

        this.albumScreenViewModel =
            AlbumScreenViewModel(
                mediaRepository = testMediaRepository,
                savedStateHandle = savedStateHandle
            )

        composeTestRule.setContent {
            AlbumScreen(
                viewModel = albumScreenViewModel
            )
        }

        runBlocking { composeTestRule.awaitIdle() }
        composeTestRule.onNodeWithText(expectedSong1Title).assertExists()
        composeTestRule.onNodeWithText(expectedSong2Title).assertExists()

    }

}