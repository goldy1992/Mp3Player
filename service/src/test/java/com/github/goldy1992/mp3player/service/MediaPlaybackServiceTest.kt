package com.github.goldy1992.mp3player.service

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.MediaBrowserServiceCompat.Result
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode
import java.util.*

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MediaPlaybackServiceTest {
    /** object to test */
    lateinit var mediaPlaybackService: TestMediaPlaybackServiceInjector

    private val rootAuthenticator: RootAuthenticator = mock<RootAuthenticator>()

    @Before
    fun setup() {
        mediaPlaybackService = Robolectric.buildService(TestMediaPlaybackServiceInjector::class.java).create().get()
        mediaPlaybackService.setHandler(Handler(Looper.getMainLooper()))
        Shadows.shadowOf(mediaPlaybackService.worker!!.looper).idle()
        mediaPlaybackService.setRootAuthenticator(rootAuthenticator)
    }

    @Test
    fun testGetRoot() {
        val browserRoot = MediaBrowserServiceCompat.BrowserRoot(ACCEPTED_MEDIA_ROOT_ID, null)
        val clientPackageName = "packageName"
        val clientUid = 45
        val rootHints: Bundle? = null
        whenever(rootAuthenticator!!.authenticate(clientPackageName, clientUid, rootHints)).thenReturn(browserRoot)
        val result = mediaPlaybackService!!.onGetRoot(clientPackageName, clientUid, rootHints)
        Shadows.shadowOf(mediaPlaybackService.worker!!.looper).idle()

        Assert.assertNotNull(result)
        Assert.assertEquals(ACCEPTED_MEDIA_ROOT_ID, result!!.rootId)

    }

    @Test
    fun testOnLoadChildrenWithRejectedRootId() {
        whenever(rootAuthenticator!!.rejectRootSubscription(any())).thenReturn(true)
        val parentId = "aUniqueId"
        val result: Result<List<MediaBrowserCompat.MediaItem>> = mock<Result<List<MediaBrowserCompat.MediaItem>>>() as Result<List<MediaBrowserCompat.MediaItem>> //Mockito.mock<Result<List<MediaBrowserCompat.MediaItem>>>(Result::class.java)
        mediaPlaybackService!!.onLoadChildren(parentId, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(result, times(1)).sendResult(null)
    }

    @Test
    fun testOnLoadChildrenWithAcceptedMediaId() {
        val parentId = "aUniqueId"
        val result: Result<List<MediaBrowserCompat.MediaItem>> = mock<Result<List<MediaBrowserCompat.MediaItem>>>() as Result<List<MediaBrowserCompat.MediaItem>>
        val mockContentManager = mock<ContentManager>()
        mediaPlaybackService!!.contentManager = mockContentManager
        val mediaItemList: List<MediaBrowserCompat.MediaItem> = ArrayList()
        whenever(mockContentManager.getChildren(any<String>())).thenReturn(mediaItemList)
        mediaPlaybackService!!.onLoadChildren(parentId, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(result, times(1)).sendResult(mediaItemList)
    }

    @Test
    fun testOnLoadChildrenRejectedMediaId() {
        whenever(rootAuthenticator!!.rejectRootSubscription(any())).thenReturn(true)
        val result: Result<List<MediaBrowserCompat.MediaItem>> = mock<Result<List<MediaBrowserCompat.MediaItem>>>() as Result<List<MediaBrowserCompat.MediaItem>>
        mediaPlaybackService!!.onLoadChildren(REJECTED_MEDIA_ROOT_ID, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(result, times(1)).sendResult(null)
    }

    companion object {
        private const val ACCEPTED_MEDIA_ROOT_ID = "ACCEPTED"
        private const val REJECTED_MEDIA_ROOT_ID = "REJECTED"
    }
}