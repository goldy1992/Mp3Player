package com.github.goldy1992.mp3player.service

import androidx.media3.common.MediaItem
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class PlaylistManagerTest {
    private var playlistManager: PlaylistManager? = null
    @Before
    fun setup() {
        val queueItems: List<MediaItem> = ArrayList()
        playlistManager = PlaylistManager(queueItems.toMutableList())
    }

    @Test
    fun testCreateNewPlaylist() {
        val originalPlaylist: List<MediaItem> = emptyList()
        playlistManager = PlaylistManager(originalPlaylist.toMutableList())
        Assert.assertEquals(originalPlaylist, playlistManager!!.playlist)
        val newPlaylist: List<MediaItem?> = emptyList()
        playlistManager!!.createNewPlaylist(newPlaylist)
        Assert.assertEquals(newPlaylist, playlistManager!!.playlist)
    }

    companion object {
        private val MOCK_QUEUE_ITEM = mock<MediaItem>()
    }
}