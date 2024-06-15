package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SongsTest {

    private val context = InstrumentationRegistry.getInstrumentation().context

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoilApi::class)
    @Test
    fun displaySongs() {
        val id1 = "id1"
        val id2 = "id2"
        val artist1 = "artist1"
        val artist2 = "artist2"
        val title1 = "title1"
        val title2 = "title2"

        val song1 = Song(
            id = id1,
            title = title1,
            duration = 20560L,
            artist = artist1
        )

        val song2 = Song(
            id = id2,
            title = title2,
            duration = 50751L,
            artist = artist2
        )

        val songsListContentDescr = context.getString(R.string.songs_list)

        val songList : List<Song> = listOf(song1, song2)
        val playlist = Playlist(state = State.LOADED,
        songs = songList
        )

        composeTestRule.setContent {
            SongList(playlist = playlist,
                isPlayingProvider = { false },
                currentSongProvider = { Song()  },
                onSongSelected = {_,_ ->},
            )
        }

        val assertText = { text: String ->
            composeTestRule.onNodeWithText(text).assertExists(
            "Could not file $text in semantic tree.")
        }
        assertText(title1)
        assertText(title2)
        val node = composeTestRule.onNodeWithContentDescription(songsListContentDescr)
        val children = node.fetchSemanticsNode().children
        assertEquals(2, children.size)
    }
}