package com.github.goldy1992.mp3player.service.player

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.MyControlDispatcher
import com.github.goldy1992.mp3player.service.PlaylistManager
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class MyPlaybackPreparerTest {
    @Mock
    private val contentManager: ContentManager? = null
    @Mock
    private val exoPlayer: ExoPlayer? = null
    @Mock
    private val mediaSourceFactory: MediaSourceFactory? = null
    @Mock
    private val myControlDispatcher: MyControlDispatcher? = null
    @Mock
    private val playlistManager: PlaylistManager? = null
    @Mock
    private val mediaSource: MediaSource? = null
    private var myPlaybackPreparer: MyPlaybackPreparer? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val testItem = MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build()
        Mockito.`when`<List<MediaBrowserCompat.MediaItem>>(playlistManager!!.playlist).thenReturn(listOf(testItem))
        Mockito.`when`(mediaSourceFactory!!.createMediaSource(ArgumentMatchers.any<Uri>())).thenReturn(mediaSource)
        myPlaybackPreparer = MyPlaybackPreparer(exoPlayer!!, contentManager!!, mediaSourceFactory, myControlDispatcher!!, playlistManager)
    }

    @Test
    fun testSupportedActions() {
        val items: List<MediaBrowserCompat.MediaItem> = ArrayList()
        myPlaybackPreparer = MyPlaybackPreparer(exoPlayer!!, contentManager!!, mediaSourceFactory!!, myControlDispatcher!!, playlistManager!!)
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
        Mockito.verify(myControlDispatcher, Mockito.times(1))!!.dispatchSetPlayWhenReady(exoPlayer!!, false)
        // should seek to the first index, position 0
        Mockito.verify(myControlDispatcher, Mockito.times(1))!!.dispatchSeekTo(exoPlayer, 0, 0)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testOnPrepareFromSearch() {
        myPlaybackPreparer!!.onPrepareFromSearch("query", true, null)
    }

    @Test
    fun testOnPrepareFromUri() {
        val testUri = Uri.parse("query")
        val testItem = MediaItemBuilder("id1").setMediaUri(Uri.parse("string")).build()
        Mockito.`when`(contentManager!!.getItem(testUri)).thenReturn(testItem)
        myPlaybackPreparer!!.onPrepareFromUri(testUri, true, null)
        Mockito.verify(playlistManager, Mockito.times(1))!!.createNewPlaylist(ArgumentMatchers.any<List<MediaBrowserCompat.MediaItem?>>())
    }

    @Test
    fun testOnCommand() {
        Assert.assertFalse(myPlaybackPreparer!!.onCommand(exoPlayer!!, Mockito.mock(ControlDispatcher::class.java), "query", null, null))
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
        Mockito.`when`(contentManager!!.getPlaylist(mediaId)).thenReturn(items)
        myPlaybackPreparer!!.onPrepareFromMediaId(mediaId, true, null)
        Mockito.verify(myControlDispatcher, Mockito.times(1))!!.dispatchSeekTo(exoPlayer, 1, 0)
    }

    private fun assertContainsAction(@PlaybackStateCompat.Actions action: Long) {
        val actions = myPlaybackPreparer!!.supportedPrepareActions
        val containsAction = 0L != actions and action
        Assert.assertTrue(containsAction)
    }
}