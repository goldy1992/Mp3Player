package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.platform.app.InstrumentationRegistry
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.ui.states.State
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
        val songs = Songs(state = State.LOADED,
        songs = songList
        )

        composeTestRule.setContent {
            SongList(songs = songs,
                isPlayingProvider = { false },
                currentSongProvider = { Song()  },
                onSongSelected = {_,_ ->},
            )
        }

        val node = composeTestRule.onNodeWithContentDescription(songsListContentDescr)
        node.onChildAt(0).onChildren().assertAny(hasText("title1"))
        node.onChildAt(1).onChildren().assertAny(hasText("title2"))
        val children = node.fetchSemanticsNode().children
        assertEquals(2, children.size)
    }
}