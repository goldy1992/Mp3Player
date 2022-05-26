package com.github.goldy1992.mp3player.service.player

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.PlaylistManager
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ForwardingPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class MyPlaybackPreparerTest {

    private val contentManager: ContentManager = mock<ContentManager>()
    
    private val exoPlayer: ExoPlayer = mock<ExoPlayer>()

    private val mediaSourceFactory: MediaSource.Factory = mock<MediaSource.Factory>()

    private val myControlDispatcher: ForwardingPlayer = mock<ForwardingPlayer>()

    private val playlistManager: PlaylistManager = mock<PlaylistManager>()

    private val mediaSource: MediaSource = mock<MediaSource>()
    private var myPlaybackPreparer: MyPlaybackPreparer? = null

    @Before
    fun setup() {
        val testItem = MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build()
        whenever(playlistManager.playlist).thenReturn(mutableListOf(testItem))
        whenever(mediaSourceFactory.createMediaSource(any<MediaItem>())).thenReturn(mediaSource)
        myPlaybackPreparer = MyPlaybackPreparer(exoPlayer, contentManager, mediaSourceFactory, myControlDispatcher, playlistManager)
    }

    @Test
    fun testSupportedActions() {
        myPlaybackPreparer = MyPlaybackPreparer(exoPlayer, contentManager, mediaSourceFactory, myControlDispatcher, playlistManager)
        assertContainsAction(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)
        assertContainsAction(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH)
    }

    /**
     * When the PlaybackPreparer is created then items are prepared, but not played
     * @throws FileDataSource.FileDataSourceException
     */
    @Test
    @Throws(FileDataSourceException::class)
    fun testPreparePlaylistOnConstruct() { // don't play when being constructed
        verify(myControlDispatcher, times(1)).playWhenReady =  false
        // should seek to the first index, position 0
        verify(myControlDispatcher, times(1)).seekTo( 0, 0)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testOnPrepareFromSearch() {
        myPlaybackPreparer!!.onPrepareFromSearch("query", true, null)
    }

    @Test
    fun testOnPrepareFromUri() {
        val testUri = Uri.parse("query")
        val testItem = MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build()
        whenever(contentManager.getItem(testUri)).thenReturn(testItem)
        myPlaybackPreparer!!.onPrepareFromUri(testUri, true, null)
        verify(playlistManager, times(1)).createNewPlaylist(any<List<MediaBrowserCompat.MediaItem?>>())
    }

    @Test
    fun testOnCommand() {
        Assert.assertFalse(myPlaybackPreparer!!.onCommand(exoPlayer,"query", null, null))
    }

    @Test
    fun testPrepareFromMediaId() {
        val trackId = "id2"
        val mediaId = "x|$trackId"
        val testItem1 = MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build()
        val testItem2 = MediaItemBuilder(trackId).setMediaUri(Uri.parse("string")).build()
        val testItem3 = MediaItemBuilder("id3").setMediaUri(Uri.parse("string")).build()
        val items: MutableList<MediaBrowserCompat.MediaItem> = ArrayList()
        items.add(testItem1)
        items.add(testItem2)
        items.add(testItem3)
        whenever(contentManager.getPlaylist(mediaId)).thenReturn(items)
        myPlaybackPreparer!!.onPrepareFromMediaId(mediaId, true, null)
        verify(myControlDispatcher, times(1)).seekTo(1, 0)
    }

    private fun assertContainsAction(@PlaybackStateCompat.Actions action: Long) {
        val actions = myPlaybackPreparer!!.supportedPrepareActions
        val containsAction = 0L != actions and action
        Assert.assertTrue(containsAction)
    }
}