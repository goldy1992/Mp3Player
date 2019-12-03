package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import java.util.*

@RunWith(RobolectricTestRunner::class)
class MediaIdSubscriptionCallbackTest {
    private var mediaIdSubscriptionCallback: MediaIdSubscriptionCallback? = null
    @Mock
    private val mediaBrowserResponseListener: MediaBrowserResponseListener? = null
    private var mediaItemList: List<MediaBrowserCompat.MediaItem>? = null

    @Mock
    private var mockMediaItem: MediaBrowserCompat.MediaItem? = null
    @Captor
    var captor: ArgumentCaptor<ArrayList<MediaBrowserCompat.MediaItem>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val handler = Handler(Looper.getMainLooper())
        mockMediaItem = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
        mediaIdSubscriptionCallback = MediaIdSubscriptionCallback(handler)
        mediaItemList = listOf(mockMediaItem!!)
    }

    @Test
    fun testRegisterListener() {
        val key = "KEY"
        Assert.assertTrue(mediaIdSubscriptionCallback!!.getMediaBrowserResponseListeners().isEmpty())
        mediaIdSubscriptionCallback!!.registerMediaBrowserResponseListener(key, mediaBrowserResponseListener)
        // assert size is now 1
        val restultSize = mediaIdSubscriptionCallback!!.getMediaBrowserResponseListeners().size
        Assert.assertEquals(1, restultSize.toLong())
    }

    @Test
    fun testOnChildrenLoadedForSubscribedKey() {
        val subscribedKey = "SubscribedKey"
        mediaIdSubscriptionCallback!!.registerMediaBrowserResponseListener(subscribedKey, mediaBrowserResponseListener)
        mediaIdSubscriptionCallback!!.onChildrenLoaded(subscribedKey, mediaItemList!!)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(mediaBrowserResponseListener, Mockito.times(1))!!.onChildrenLoaded(ArgumentMatchers.eq(subscribedKey), captor!!.capture())
        val children = captor!!.value
        Assert.assertEquals(1, children.size.toLong())
        Assert.assertEquals(mockMediaItem, children[0])
    }

    @Test
    fun testOnChildrenLoadedForNonSubscribedKey() {
        val subscribedKey = "SubscribedKey"
        val nonSubscribedKey = "NonSubscribedKey"
        mediaIdSubscriptionCallback!!.registerMediaBrowserResponseListener(subscribedKey, mediaBrowserResponseListener)
        mediaIdSubscriptionCallback!!.onChildrenLoaded(nonSubscribedKey, mediaItemList!!)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(mediaBrowserResponseListener, Mockito.never())!!.onChildrenLoaded(ArgumentMatchers.eq(nonSubscribedKey), ArgumentMatchers.any())
    }
}