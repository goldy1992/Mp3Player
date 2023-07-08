package com.github.goldy1992.mp3player.client.data.repositories.media

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaEntityParserTest {

    @Test
    fun testCanParseRootWithNoResults() {
        val root : Root = Root.NO_RESULTS
        val mediaItems = emptyList<MediaItem>()
        val result = MediaEntityParser.parse(root, mediaItems)
        assertTrue(result.childMap.isEmpty())
        assertEquals(State.NO_RESULTS, result.state)
    }

    @Test
    fun testCanParseRootWithChildren() {
        val root : Root = Root.NOT_LOADED
        val albums = MediaItemBuilder("albums")
            .setMediaItemType(MediaItemType.ALBUMS)
            .build()
        val songs = MediaItemBuilder("songs")
            .setMediaItemType(MediaItemType.SONGS)
            .build()
        val mediaItems = listOf(songs, albums)

        val result = MediaEntityParser.parse(root, mediaItems)
        assertTrue(result.childMap.isNotEmpty())
        assertTrue(result.childMap[MediaItemType.SONGS] is Playlist)
        assertTrue(result.childMap[MediaItemType.ALBUMS] is Albums)
        assertEquals(State.LOADED, result.state)
    }
}