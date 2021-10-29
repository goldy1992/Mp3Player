package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

class SongsTest {

    @Mock
    private val mockMediaController = mock<MediaControllerAdapter>()

    private val context = InstrumentationRegistry.getInstrumentation().context

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaySongs() {
        val songsListContentDescr = context.getString(R.string.songs_list)
        val song1 = MediaItemBuilder("id1").setTitle("title1")
                .setMediaItemType(MediaItemType.SONG)
                .setArtist("artist1")
                .build()
        val song2 = MediaItemBuilder("id2").setTitle("title2")
                .setMediaItemType(MediaItemType.SONG)
                .setArtist("artist2")
                .build()
        val list : List<MediaBrowserCompat.MediaItem> = listOf<MediaBrowserCompat.MediaItem>(song1, song2)
        val songsData: MutableLiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData()
        songsData.postValue(list)

        composeTestRule.setContent {
            SongList(songsData,
                    mockMediaController)
        }
        runBlocking {
            composeTestRule.awaitIdle()
            val node = composeTestRule.onNodeWithContentDescription(songsListContentDescr)
            node.onChildAt(0).assert(hasText("title1"))
            node.onChildAt(1).assert(hasText("title2"))
            val children = node.fetchSemanticsNode().children
            assertEquals(2, children.size)
        }
    }
}