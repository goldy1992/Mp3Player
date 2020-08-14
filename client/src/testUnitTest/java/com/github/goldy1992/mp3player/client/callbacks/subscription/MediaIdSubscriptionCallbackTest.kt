package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.os.Looper
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val mediaIdSubscriptionCallback: MediaIdSubscriptionCallback = MediaIdSubscriptionCallback()
    private lateinit var mediaBrowserSubscriber: MediaBrowserSubscriber
    private lateinit var mediaItemList: List<MediaItem>


    private val mockMediaItem: MediaItem = mock<MediaItem>()

    @Before
    fun setup() {
        this.mediaBrowserSubscriber = mock<MediaBrowserSubscriber>()
        mediaItemList = listOf(mockMediaItem)
    }

    @Test
    fun testRegisterListener() {
        val key = "KEY"
        Assert.assertTrue(mediaIdSubscriptionCallback.getMediaBrowserSubscribers().isEmpty())
        mediaIdSubscriptionCallback.registerMediaBrowserSubscriber(key, mediaBrowserSubscriber)
        // assert size is now 1
        val restultSize = mediaIdSubscriptionCallback.getMediaBrowserSubscribers().size
        Assert.assertEquals(1, restultSize.toLong())
    }

    @Test
    fun testOnChildrenLoadedForSubscribedKey() {
        val subscribedKey = "SubscribedKey"
        val resultObject : LiveData<List<MediaItem>> = mediaIdSubscriptionCallback.subscribe(subscribedKey)
        mediaIdSubscriptionCallback.onChildrenLoaded(subscribedKey, mediaItemList)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val children = resultObject.value
        Assert.assertEquals(1, children?.size)
        Assert.assertEquals(mockMediaItem, children?.first())
    }
}