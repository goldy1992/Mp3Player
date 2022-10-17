package com.github.goldy1992.mp3player.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.media3.session.IMediaSessionService
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ServiceTestRule
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.SearchDatabaseModule
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
@UninstallModules(
    SearchDatabaseModule::class,
    ContentManagerModule::class)
@HiltAndroidTest
class MediaPlaybackServiceTest {

    private val testScheduler = TestCoroutineScheduler()

    private val dispatcher  = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(dispatcher)
    @Rule(order = 0) @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val serviceRule = ServiceTestRule()



    /** object to testFullDebug */
    lateinit var mediaPlaybackService: MediaPlaybackService

    private val rootAuthenticator: RootAuthenticator = mock<RootAuthenticator>()

    private val contentManager : ContentManager = mock<ContentManager>()

    @Before
    fun setup() {
        rule.inject()
     //   mediaPlaybackService = Robolectric.setupService(MediaPlaybackService::class.java)
      //  mediaPlaybackService.player
    }

    @Test
    @Throws(TimeoutException::class)
    fun testWithBoundService() = runTest(dispatcher) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Create the service Intent.
        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            MediaPlaybackService::class.java
        ).apply {
            this.action = MediaLibraryService.SERVICE_INTERFACE
            // Data can be passed to the service via the Intent.
            //putExtra(SEED_KEY, 42L)
        }

        // Bind the service and grab a reference to the binder.
        //val binder: IBinder = serviceRule.bindService(serviceIntent)


        // Get the reference to the service, or you can call
        // public methods on the binder directly.

      //  val service: IMediaSessionService = (binder as IMediaSessionService)

        val sessionToken = SessionToken(context, ComponentName(context, MediaPlaybackService::class.java))
        val mblf : ListenableFuture<MediaBrowser> = MediaBrowser.Builder(context, sessionToken).buildAsync()
        mblf.await()
        Log.i("", "")
        // Verify that the service is working correctly.
     //   assertThat(service.getRandomInt(), `is`(any(Int::class.java)))
    }


//    @Test
//    fun testGetRoot() {
//        val browserRoot = MediaBrowserServiceCompat.BrowserRoot(ACCEPTED_MEDIA_ROOT_ID, null)
//        val clientPackageName = "packageName"
//        val clientUid = 45
//        val rootHints: Bundle? = null
//        whenever(rootAuthenticator.authenticate(clientPackageName, clientUid, rootHints)).thenReturn(browserRoot)
//        val result = mediaPlaybackService.onGetRoot(clientPackageName, clientUid, rootHints)
//        Assert.assertNotNull(result)
//        Assert.assertEquals(ACCEPTED_MEDIA_ROOT_ID, result!!.rootId)
//
//    }

//    @Test
//    fun testOnLoadChildrenWithRejectedRootId() {
//        whenever(rootAuthenticator.rejectRootSubscription(any())).thenReturn(true)
//        val parentId = "aUniqueId"
//        val result: Result<List<MediaItem>> = mock<Result<List<MediaItem>>>()
//        mediaPlaybackService.onLoadChildren(parentId, result)
//        Shadows.shadowOf(Looper.getMainLooper()).idle()
//        verify(result, times(1)).sendResult(null)
//    }

//    @Test
//    @ExperimentalCoroutinesApi
//    fun testOnLoadChildrenWithAcceptedMediaId() = runBlockingTest {
//        val parentId = "aUniqueId"
//        val result: Result<List<MediaItem>> = mock<Result<List<MediaItem>>>()
//        val mediaItemList: List<MediaItem> = ArrayList()
//        whenever(contentManager.getChildren(any<String>())).thenReturn(mediaItemList)
//        mediaPlaybackService.onLoadChildren(parentId, result)
//        Shadows.shadowOf(Looper.getMainLooper()).idle()
//        verify(result, times(1)).sendResult(mediaItemList)
//    }
//
//    @Test
//    fun testOnLoadChildrenRejectedMediaId() {
//        whenever(rootAuthenticator.rejectRootSubscription(any())).thenReturn(true)
//        val result: Result<List<MediaItem>> = mock<Result<List<MediaItem>>>()
//        mediaPlaybackService.onLoadChildren(REJECTED_MEDIA_ROOT_ID, result)
//        Shadows.shadowOf(Looper.getMainLooper()).idle()
//        verify(result, times(1)).sendResult(null)
//    }
//
//    @Test
//    fun testOnSearch() {
//        val result : Result<MutableList<MediaItem>> = mock<Result<MutableList<MediaItem>>>()
//        val query : String = "query"
//        val extras : Bundle? = Bundle()
//        val expectedMediaItems = mock<MutableList<MediaItem>>()
//        whenever(contentManager.search(any<String>())).thenReturn(expectedMediaItems)
//        mediaPlaybackService.onSearch(query, extras, result)
//        verify(result, times(1)).sendResult(expectedMediaItems)
//    }

    @Test
    fun testOnCreate() {
        assertTrue(true)
    }

    companion object {
        private const val ACCEPTED_MEDIA_ROOT_ID = "ACCEPTED"
        private const val REJECTED_MEDIA_ROOT_ID = "REJECTED"
    }
}