package com.github.goldy1992.mp3player.client.activities

import android.os.Looper
import android.os.Looper.getMainLooper
import android.support.v4.media.MediaBrowserCompat
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.test.core.app.ActivityScenario
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import java.util.*

@HiltAndroidTest
@UninstallModules(GlideModule::class,
        MediaBrowserAdapterModule::class,
        MediaControllerAdapterModule::class)
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

    @Test
    fun testOnItemSelected() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            val result = activity.onOptionsItemSelected(menuItem)
            shadowOf(getMainLooper()).idle()
            Assert.assertFalse(result)
        }
    }

    @Test
    fun testOnItemSelectedHomeButton() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(android.R.id.home)
            val result = activity.onOptionsItemSelected(menuItem)
            Assert.assertTrue(result)
        }
    }

    // new tests
    @Test
    fun testOnOptionsItemSelectedOpenDrawer() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(android.R.id.home)
            activity.onOptionsItemSelected(menuItem)
            activity.drawerLayout?.isDrawerOpen(GravityCompat.START)
        }
    }

    @Test
    fun testOnOptionsItemSelectedSearch() {
        scenario.onActivity { activity: MainActivity ->
            val menuItem = mock<MenuItem>()
            // select the search option item
            activity.onOptionsItemSelected(menuItem)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
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

}