package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

import kotlin.collections.ArrayList

@RunWith(RobolectricTestRunner::class)
class MediaIdSubscriptionCallbackTest {
    private var mediaIdSubscriptionCallback: MediaIdSubscriptionCallback? = null

    private lateinit var mediaBrowserResponseListener: MediaBrowserResponseListener
    private var mediaItemList: List<MediaBrowserCompat.MediaItem>? = null

    @Mock
    private var mockMediaItem: MediaBrowserCompat.MediaItem? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.mediaBrowserResponseListener = mock<MediaBrowserResponseListener>()
        mockMediaItem = Mockito.mock(MediaBrowserCompat.MediaItem::class.java)
        mediaIdSubscriptionCallback = MediaIdSubscriptionCallback()
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
        argumentCaptor<ArrayList<MediaBrowserCompat.MediaItem>>().apply {
            val subscribedKey = "SubscribedKey"
            mediaIdSubscriptionCallback!!.registerMediaBrowserResponseListener(subscribedKey, mediaBrowserResponseListener)
            mediaIdSubscriptionCallback!!.onChildrenLoaded(subscribedKey, mediaItemList!!)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            verify(mediaBrowserResponseListener, times(1)).onChildrenLoaded(eq(subscribedKey), capture())
            val children = allValues
            Assert.assertEquals(1, children.size.toLong())
            Assert.assertEquals(mockMediaItem, children[0][0])

        }

    }

    @Test
    fun testOnChildrenLoadedForNonSubscribedKey() {
        val subscribedKey = "SubscribedKey"
        val nonSubscribedKey = "NonSubscribedKey"
        mediaIdSubscriptionCallback!!.registerMediaBrowserResponseListener(subscribedKey, mediaBrowserResponseListener)
        mediaIdSubscriptionCallback!!.onChildrenLoaded(nonSubscribedKey, mediaItemList!!)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(mediaBrowserResponseListener, never()).onChildrenLoaded(eq(nonSubscribedKey), any())
    }
}