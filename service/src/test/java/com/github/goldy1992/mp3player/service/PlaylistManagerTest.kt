package com.github.goldy1992.mp3player.service

import android.support.v4.media.MediaBrowserCompat
import edu.emory.mathcs.backport.java.util.Collections
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class PlaylistManagerTest {
    private var playlistManager: PlaylistManager? = null
    @Before
    fun setup() {
        val queueItems: List<MediaBrowserCompat.MediaItem> = ArrayList()
        playlistManager = PlaylistManager(queueItems.toMutableList())
    }

    @Test
    fun testCreateNewPlaylist() {
        val originalPlaylist: List<MediaBrowserCompat.MediaItem> = Collections.emptyList() as List<MediaBrowserCompat.MediaItem>
        playlistManager = PlaylistManager(originalPlaylist.toMutableList())
        Assert.assertEquals(originalPlaylist, playlistManager!!.playlist)
        val newPlaylist: List<MediaBrowserCompat.MediaItem?> = emptyList()
        playlistManager!!.createNewPlaylist(newPlaylist)
        Assert.assertEquals(newPlaylist, playlistManager!!.playlist)
    }

    companion object {
        private val MOCK_QUEUE_ITEM = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
    }
}