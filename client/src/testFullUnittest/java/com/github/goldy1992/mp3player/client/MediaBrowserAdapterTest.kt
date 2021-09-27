package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Test class for [MediaBrowserAdapter].
 */
class MediaBrowserAdapterTest {

    private lateinit var mediaBrowserAdapter : MediaBrowserAdapter
    private val mySubscriptionCallback = mock<MediaIdSubscriptionCallback>()
    private val mySearchCallback = mock<MySearchCallback>()
    private val mockMediaBrowserCompat = mock<MediaBrowserCompat>()

    /** Setup method */
    @Before
    fun setup() {
         mediaBrowserAdapter = MediaBrowserAdapter(mockMediaBrowserCompat, mySubscriptionCallback, mySearchCallback)
    }

    /** Tests [MediaBrowserAdapter.isConnected] */
    @Test
    fun testConnectWhenAlreadyConnected() {
        whenever(mockMediaBrowserCompat.isConnected).thenReturn(true)
        mediaBrowserAdapter.connect()
        verify(mockMediaBrowserCompat, never()).connect()

    }

    /** Tests [MediaBrowserAdapter.isConnected] */
    @Test
    fun testConnectWhenNotConnected() {
        whenever(mockMediaBrowserCompat.isConnected).thenReturn(false)
        mediaBrowserAdapter.connect()
        verify(mockMediaBrowserCompat, times(1)).connect()

    }

    /** Tests [MediaBrowserAdapter.subscribe] */
    @Test
    fun testSubscribe() {
        val id = "xyz"
        val expectedLiveData = mock<LiveData<List<MediaBrowserCompat.MediaItem>>>()
        whenever(mySubscriptionCallback.subscribe(id)).thenReturn(expectedLiveData)

        val result = mediaBrowserAdapter.subscribe(id)
        assertEquals(expectedLiveData, result)
        verify(mySubscriptionCallback, times(1)).subscribe(id)
        verify(mockMediaBrowserCompat, times(1)).subscribe(id, mySubscriptionCallback)
    }

    /** Tests [MediaBrowserAdapter.search] */
    @Test
    fun testSearch() {
        val query = "query"
        val extras = mock<Bundle>()

        mediaBrowserAdapter.search(query, extras)

        verify(mockMediaBrowserCompat, times(1)).search(query, extras, mySearchCallback)
    }

    /** Tests [MediaBrowserAdapter.onConnected] */
    @Test
    fun testOnConnected() {
        val expectedRootId = "rootId"
        whenever(mockMediaBrowserCompat.root).thenReturn(expectedRootId)

        mediaBrowserAdapter.onConnected()

        verify(mySubscriptionCallback, times(1)).subscribeRoot(expectedRootId)
        verify(mockMediaBrowserCompat, times(1)).subscribe(expectedRootId, mySubscriptionCallback)

    }

}