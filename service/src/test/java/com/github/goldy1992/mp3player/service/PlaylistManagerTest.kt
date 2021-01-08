package com.github.goldy1992.mp3player.service

import android.support.v4.media.MediaBrowserCompat
import com.nhaarman.mockitokotlin2.mock

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

import kotlin.collections.ArrayList
import kotlin.collections.emptyList


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
        val originalPlaylist: List<MediaBrowserCompat.MediaItem> = emptyList()
        playlistManager = PlaylistManager(originalPlaylist.toMutableList())
        Assert.assertEquals(originalPlaylist, playlistManager!!.playlist)
        val newPlaylist: List<MediaBrowserCompat.MediaItem?> = emptyList()
        playlistManager!!.createNewPlaylist(newPlaylist)
        Assert.assertEquals(newPlaylist, playlistManager!!.playlist)
    }

    companion object {
        private val MOCK_QUEUE_ITEM = mock<MediaBrowserCompat.MediaItem>()
    }
}