package com.github.goldy1992.mp3player.client.activities

import android.os.Looper.getMainLooper
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.ui.onNavDestinationSelected
import androidx.test.core.app.ActivityScenario
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@UninstallModules(GlideModule::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    private lateinit var scenario : ActivityScenario<MainActivity>

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity -> activity.onConnected() }
    }

    @Ignore
    // TODO: move to instrumented test
    @Test
    fun testOnItemSelected() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.onNavDestinationSelected(any())).thenReturn(true)
            val result = activity.onOptionsItemSelected(menuItem)
            shadowOf(getMainLooper()).idle()
            assertTrue(result)
        }
    }

    @Test
    fun testOnItemSelectedHomeButton() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(android.R.id.home)
            val result = activity.onOptionsItemSelected(menuItem)
            shadowOf(getMainLooper()).idle()
            Assert.assertTrue(result)
        }

    }

    // new tests
    // TODO: move to instrumented test
    @Ignore
    @Test
    fun testOnOptionsItemSelectedOpenDrawer() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(android.R.id.home)
            activity.onOptionsItemSelected(menuItem)
            activity.drawerLayout?.isDrawerOpen(GravityCompat.START)
        }
    }

    // TODO: Move to MainActivityViewModelTest
//    @Test
//    fun testOnChildrenLoadedForRootCategory() {
//        scenario.onActivity { activity: MainActivity ->
//            var myPageAdapterSpied = spy(activity.adapter)
//            doNothing().whenever(myPageAdapterSpied).notifyDataSetChanged()
//            activity.adapter = myPageAdapterSpied;
//            val parentId = "parentId"
//            val children = ArrayList<MediaBrowserCompat.MediaItem>()
//            val rootItemsSet: Set<MediaItemType> = MediaItemType.PARENT_TO_CHILD_MAP[MediaItemType.ROOT]!!
//            for (category in rootItemsSet) {
//                val mediaItem = MediaItemBuilder("id1")
//                        .setTitle(category.title)
//                        .setDescription(category.description)
//                        .setRootItemType(category)
//                        .setMediaItemType(MediaItemType.ROOT)
//                        .build()
//                children.add(mediaItem)
//            }
//            val expectedNumOfFragmentsCreated = rootItemsSet.size
//            activity.onChildrenLoaded(parentId, children)
//            Shadows.shadowOf(Looper.getMainLooper()).idle()
//            val numberOfChildFragments = activity.adapter.itemCount
//            Assert.assertEquals(expectedNumOfFragmentsCreated.toLong(), numberOfChildFragments.toLong())
//        }
//    }

    /* TODO: use these tests when intents to play a song from file is supported
    @Test
    fun testOnCreateViewIntent() {
        intent!!.action = Intent.ACTION_VIEW
        val expectedUri = mock<Uri>()
        intent!!.data = expectedUri
        launchActivityAndConnect()
        scenario.onFragment { activity: MediaPlayerFragment ->
            Assert.assertEquals(expectedUri, activity.trackToPlay)
        }
    }

    @Test
    fun testOnNewIntentWithoutViewAction() {
        launchActivityAndConnect()
        scenario.onFragment { activity: MediaPlayerFragment ->
            val newIntent = Intent(context, MediaPlayerFragment::class.java)
            val testUri = mock<Uri>()
            newIntent.data = testUri
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
            //    activity.onNewIntent(newIntent)
            verify(spiedMediaControllerAdapter, never()).playFromUri(testUri, null)
        }
    }

    @Test
    fun testOnNewIntentWithViewAction() {
        launchActivityAndConnect()
        scenario.onFragment { activity: MediaPlayerFragment ->
            val newIntent = Intent(context, MediaPlayerFragment::class.java)
            val testUri = mock<Uri>()
            newIntent.data = testUri
            newIntent.action = Intent.ACTION_VIEW
            val spiedMediaControllerAdapter = spy(activity.mediaControllerAdapter)
            activity.mediaControllerAdapter = spiedMediaControllerAdapter
        //    activity.onNewIntent(newIntent)
            verify(spiedMediaControllerAdapter, times(1)).playFromUri(testUri, null)
        }
    }

    private fun launchActivityAndConnect() {
        scenario = ActivityScenario.launch(intent)
        scenario.onFragment { activity -> activity.onConnected() }
    }
    */


}