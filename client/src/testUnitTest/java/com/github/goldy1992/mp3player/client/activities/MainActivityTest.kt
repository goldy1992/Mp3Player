package com.github.goldy1992.mp3player.client.activities

import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.test.core.app.ActivityScenario
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode
import java.util.*

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    private lateinit var scenario : ActivityScenario<TestMainActivity>

    @Before
    fun setup() {
       scenario = ActivityScenario.launch(TestMainActivity::class.java)
    }

    @Test
    fun testOnItemSelected() {
        scenario.onActivity { activity: TestMainActivity ->
            val menuItem = mock<MenuItem>()
            val result = activity.onOptionsItemSelected(menuItem)
            Assert.assertFalse(result)
        }
    }

    @Test
    fun testOnItemSelectedHomeButton() {
        scenario.onActivity { activity: TestMainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(android.R.id.home)
            val result = activity.onOptionsItemSelected(menuItem)
            Assert.assertTrue(result)
        }
    }

    // new tests
    @Test
    fun testOnOptionsItemSelectedOpenDrawer() {
        scenario.onActivity { activity: TestMainActivity ->
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(android.R.id.home)
            activity.onOptionsItemSelected(menuItem)
            activity.drawerLayout?.isDrawerOpen(GravityCompat.START)
        }
    }

    @Test
    fun testOnOptionsItemSelectedSearch() {
        scenario.onActivity { activity: TestMainActivity ->
            val searchFragment = activity.searchFragment
            // assert the search fragment is NOT already added
            Assert.assertFalse(searchFragment!!.isAdded)
            val menuItem = mock<MenuItem>()
            whenever(menuItem.itemId).thenReturn(R.id.action_search)
            // select the search option item
            activity.onOptionsItemSelected(menuItem)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            // assert the search fragment IS now added
            Assert.assertTrue(searchFragment.isAdded)
            // post test remove the added fragment
            activity.supportFragmentManager
                    .beginTransaction()
                    .remove(activity.searchFragment!!)
                    .commit()

        }
    }

    @Test
    fun testOnChildrenLoadedForRootCategory() {
        scenario.onActivity { activity: TestMainActivity ->
            var myPageAdapterSpied = spy(activity.adapter)
            doNothing().whenever(myPageAdapterSpied!!).notifyDataSetChanged()
            activity.adapter = myPageAdapterSpied;
            val parentId = "parentId"
            val children = ArrayList<MediaBrowserCompat.MediaItem>()
            val rootItemsSet: Set<MediaItemType> = MediaItemType.PARENT_TO_CHILD_MAP[MediaItemType.ROOT]!!
            for (category in rootItemsSet) {
                val mediaItem = MediaItemBuilder("id1")
                        .setTitle(category.title)
                        .setDescription(category.description)
                        .setRootItemType(category)
                        .setMediaItemType(MediaItemType.ROOT)
                        .build()
                children.add(mediaItem)
            }
            val expectedNumOfFragmentsCreated = rootItemsSet.size
            activity.onChildrenLoaded(parentId, children)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            val numberOfChildFragments = activity.adapter!!.itemCount
            Assert.assertEquals(expectedNumOfFragmentsCreated.toLong(), numberOfChildFragments.toLong())
        }
    }
}