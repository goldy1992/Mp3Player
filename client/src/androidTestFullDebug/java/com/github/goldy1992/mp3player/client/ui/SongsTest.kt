package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.media3.common.MediaItem
import androidx.test.platform.app.InstrumentationRegistry
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
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

        val song1 = MediaItemBuilder(id1).setTitle(title1)
            .setMediaItemType(MediaItemType.SONG)
            .setArtist(artist1)
            .setDuration(20560L)
            .build()
        val song2 = MediaItemBuilder(id2).setTitle(title2)
            .setMediaItemType(MediaItemType.SONG)
            .setArtist(artist2)
            .setDuration(50751L)
            .build()

        val songsListContentDescr = context.getString(R.string.songs_list)

        val songList : List<MediaItem> = listOf(song1, song2)

        composeTestRule.setContent {
            SongList(songs = songList,
                isPlayingProvider = { false },
                currentSongProvider = { MediaItem.EMPTY  },
                onSongSelected = {_,_ ->})
        }

        val node = composeTestRule.onNodeWithContentDescription(songsListContentDescr)
        node.onChildAt(0).assert(hasText("title1"))
        node.onChildAt(1).assert(hasText("title2"))
        val children = node.fetchSemanticsNode().children
        assertEquals(2, children.size)
    }
}