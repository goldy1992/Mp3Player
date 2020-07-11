package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.os.Looper
import android.support.v4.media.MediaBrowserCompat.MediaItem
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class MediaIdSubscriptionCallbackTest {
    private var mediaIdSubscriptionCallback: MediaIdSubscriptionCallback? = null

    private lateinit var MediaBrowserSubscriber: MediaBrowserSubscriber
    private lateinit var mediaItemList: List<MediaItem>


    private val mockMediaItem: MediaItem = mock<MediaItem>()

    @Before
    fun setup() {
        this.MediaBrowserSubscriber = mock<MediaBrowserSubscriber>()
        mediaIdSubscriptionCallback = MediaIdSubscriptionCallback()
        mediaItemList = listOf(mockMediaItem)
    }

    @Test
    fun testRegisterListener() {
        val key = "KEY"
        Assert.assertTrue(mediaIdSubscriptionCallback!!.getMediaBrowserSubscribers().isEmpty())
        mediaIdSubscriptionCallback!!.registerMediaBrowserSubscriber(key, MediaBrowserSubscriber)
        // assert size is now 1
        val restultSize = mediaIdSubscriptionCallback!!.getMediaBrowserSubscribers().size
        Assert.assertEquals(1, restultSize.toLong())
    }

    @Test
    fun testOnChildrenLoadedForSubscribedKey() {
        argumentCaptor<ArrayList<MediaItem>>().apply {
            val subscribedKey = "SubscribedKey"
            mediaIdSubscriptionCallback!!.registerMediaBrowserSubscriber(subscribedKey, MediaBrowserSubscriber)
            mediaIdSubscriptionCallback!!.onChildrenLoaded(subscribedKey, mediaItemList)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            verify(MediaBrowserSubscriber, times(1)).onChildrenLoaded(eq(subscribedKey), capture())
            val children = allValues
            Assert.assertEquals(1, children.size.toLong())
            Assert.assertEquals(mockMediaItem, children[0][0])

        }

    }

    @Test
    fun testOnChildrenLoadedForNonSubscribedKey() {
        val subscribedKey = "SubscribedKey"
        val nonSubscribedKey = "NonSubscribedKey"
        mediaIdSubscriptionCallback!!.registerMediaBrowserSubscriber(subscribedKey, MediaBrowserSubscriber)
        mediaIdSubscriptionCallback!!.onChildrenLoaded(nonSubscribedKey, mediaItemList)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(MediaBrowserSubscriber, never()).onChildrenLoaded(eq(nonSubscribedKey), any())
    }
}