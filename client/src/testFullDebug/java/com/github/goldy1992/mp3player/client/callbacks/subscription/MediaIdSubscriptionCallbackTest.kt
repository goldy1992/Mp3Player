package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
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