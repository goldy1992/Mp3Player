package com.github.goldy1992.mp3player.service

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.MediaBrowserServiceCompat.Result
import com.github.goldy1992.mp3player.service.library.ContentManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
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
    @Mock
    private val rootAuthenticator: RootAuthenticator? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
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
        Mockito.`when`(rootAuthenticator!!.authenticate(clientPackageName, clientUid, rootHints)).thenReturn(browserRoot)
        val result = mediaPlaybackService!!.onGetRoot(clientPackageName, clientUid, rootHints)
        Assert.assertNotNull(result)
        Assert.assertEquals(ACCEPTED_MEDIA_ROOT_ID, result!!.rootId)
    }

    @Test
    fun testOnLoadChildrenWithRejectedRootId() {
        Mockito.`when`(rootAuthenticator!!.rejectRootSubscription(ArgumentMatchers.any())).thenReturn(true)
        val parentId = "aUniqueId"
        val result: Result<List<MediaBrowserCompat.MediaItem>> = mock(Result::class.java) as Result<List<MediaBrowserCompat.MediaItem>> //Mockito.mock<Result<List<MediaBrowserCompat.MediaItem>>>(Result::class.java)
        mediaPlaybackService!!.onLoadChildren(parentId, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(result, Mockito.times(1)).sendResult(null)
    }

    @Test
    fun testOnLoadChildrenWithAcceptedMediaId() {
        val parentId = "aUniqueId"
        val result: Result<List<MediaBrowserCompat.MediaItem>> = mock(Result::class.java) as Result<List<MediaBrowserCompat.MediaItem>>
        val mockContentManager = Mockito.mock(ContentManager::class.java)
        mediaPlaybackService!!.contentManager = mockContentManager
        val mediaItemList: List<MediaBrowserCompat.MediaItem> = ArrayList()
        Mockito.`when`(mockContentManager.getChildren(ArgumentMatchers.any<String>())).thenReturn(mediaItemList)
        mediaPlaybackService!!.onLoadChildren(parentId, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(result, Mockito.times(1)).sendResult(mediaItemList)
    }

    @Test
    fun testOnLoadChildrenRejectedMediaId() {
        Mockito.`when`(rootAuthenticator!!.rejectRootSubscription(ArgumentMatchers.any())).thenReturn(false)
        val result: Result<List<MediaBrowserCompat.MediaItem>> = mock(Result::class.java) as Result<List<MediaBrowserCompat.MediaItem>>
        mediaPlaybackService!!.onLoadChildren(REJECTED_MEDIA_ROOT_ID, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        // execute all tasks posted to main looper
//shadowOf(mediaPlaybackService.getWorker().getLooper()).idle();
        Mockito.verify(result, Mockito.times(1)).sendResult(ArgumentMatchers.any())
    }

    companion object {
        private const val ACCEPTED_MEDIA_ROOT_ID = "ACCEPTED"
        private const val REJECTED_MEDIA_ROOT_ID = "REJECTED"
    }
}