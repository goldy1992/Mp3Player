package com.github.goldy1992.mp3player.client

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.text.Typography.times


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

}