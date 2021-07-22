package com.github.goldy1992.mp3player.service

import android.os.Bundle
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.media.MediaBrowserServiceCompat
import androidx.media.MediaBrowserServiceCompat.Result
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionCompatModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.SearchDatabaseModule
import com.github.goldy1992.mp3player.service.library.ContentManager
import org.mockito.kotlin.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.util.*

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@UninstallModules(
    SearchDatabaseModule::class,
    MediaSessionCompatModule::class,
    ContentManagerModule::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class MediaPlaybackServiceTest {

    @Rule @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    /** object to test */
    lateinit var mediaPlaybackService: MediaPlaybackService

    private val rootAuthenticator: RootAuthenticator = mock<RootAuthenticator>()

    private val contentManager : ContentManager = mock<ContentManager>()

    @Before
    fun setup() {
        rule.inject()
        mediaPlaybackService = Robolectric.setupService(MediaPlaybackService::class.java)
        mediaPlaybackService.setRootAuthenticator(rootAuthenticator)
        mediaPlaybackService.setContentManager(contentManager)
    }

    @Test
    fun testGetRoot() {
        val browserRoot = MediaBrowserServiceCompat.BrowserRoot(ACCEPTED_MEDIA_ROOT_ID, null)
        val clientPackageName = "packageName"
        val clientUid = 45
        val rootHints: Bundle? = null
        whenever(rootAuthenticator.authenticate(clientPackageName, clientUid, rootHints)).thenReturn(browserRoot)
        val result = mediaPlaybackService.onGetRoot(clientPackageName, clientUid, rootHints)
        Assert.assertNotNull(result)
        Assert.assertEquals(ACCEPTED_MEDIA_ROOT_ID, result!!.rootId)

    }

    @Test
    fun testOnLoadChildrenWithRejectedRootId() {
        whenever(rootAuthenticator.rejectRootSubscription(any())).thenReturn(true)
        val parentId = "aUniqueId"
        val result: Result<List<MediaItem>> = mock<Result<List<MediaItem>>>()
        mediaPlaybackService.onLoadChildren(parentId, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(result, times(1)).sendResult(null)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testOnLoadChildrenWithAcceptedMediaId() = runBlockingTest {
        val parentId = "aUniqueId"
        val result: Result<List<MediaItem>> = mock<Result<List<MediaItem>>>()
        val mediaItemList: List<MediaItem> = ArrayList()
        whenever(contentManager.getChildren(any<String>())).thenReturn(mediaItemList)
        mediaPlaybackService.onLoadChildren(parentId, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(result, times(1)).sendResult(mediaItemList)
    }

    @Test
    fun testOnLoadChildrenRejectedMediaId() {
        whenever(rootAuthenticator.rejectRootSubscription(any())).thenReturn(true)
        val result: Result<List<MediaItem>> = mock<Result<List<MediaItem>>>()
        mediaPlaybackService.onLoadChildren(REJECTED_MEDIA_ROOT_ID, result)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(result, times(1)).sendResult(null)
    }

    @Test
    fun testOnSearch() {
        val result : Result<MutableList<MediaItem>> = mock<Result<MutableList<MediaItem>>>()
        val query : String = "query"
        val extras : Bundle? = Bundle()
        val expectedMediaItems = mock<MutableList<MediaItem>>()
        whenever(contentManager.search(any<String>())).thenReturn(expectedMediaItems)
        mediaPlaybackService.onSearch(query, extras, result)
        verify(result, times(1)).sendResult(expectedMediaItems)
    }

    companion object {
        private const val ACCEPTED_MEDIA_ROOT_ID = "ACCEPTED"
        private const val REJECTED_MEDIA_ROOT_ID = "REJECTED"
    }
}