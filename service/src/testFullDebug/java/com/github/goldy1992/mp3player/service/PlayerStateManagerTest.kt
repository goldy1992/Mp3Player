package com.github.goldy1992.mp3player.service

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.data.ISavedStateRepository
import com.github.goldy1992.mp3player.service.library.ContentManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class PlayerStateManagerTest {
    private var playerStateManager: PlayerStateManager? = null

    private val contentManager = mock<ContentManager>()
    private val iSavedStateRepository = mock<ISavedStateRepository>()
    @Before
    fun setup() {
        val queueItems: List<MediaItem> = ArrayList()
       // playerStateManager = PlayerStateManager(queueItems.toMutableList())
    }

    @Test
    fun testCreateNewPlaylist() {
        val originalPlaylist: List<MediaItem> = emptyList()
   //     playerStateManager = PlayerStateManager(originalPlaylist.toMutableList())
       // Assert.assertEquals(originalPlaylist, playerStateManager!!.playlist)
        val newPlaylist: List<MediaItem?> = emptyList()
   //     playerStateManager!!.createNewPlaylist(newPlaylist)
   //     Assert.assertEquals(newPlaylist, playerStateManager!!.playlist)
    }

    companion object {
        private val MOCK_QUEUE_ITEM = mock<MediaItem>()
    }
}