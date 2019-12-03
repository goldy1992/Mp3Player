package com.github.goldy1992.mp3player.client.activities

import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.view.MenuItem
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.LooperMode
import java.util.*

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {
    private var mainActivityTestActivityController: ActivityController<TestMainActivity>? = null
    private var mainActivity: MainActivity? = null
    @Before
    fun setup() {
        mainActivityTestActivityController = Robolectric.buildActivity(TestMainActivity::class.java).setup()
        mainActivity = mainActivityTestActivityController!!.get()
    }

    @After
    fun tearDown() {
        mainActivityTestActivityController!!.destroy()
    }

    @Test
    fun testOnItemSelected() {
        val menuItem = Mockito.mock(MenuItem::class.java)
        val result = mainActivity!!.onOptionsItemSelected(menuItem)
        Assert.assertFalse(result)
    }

    @Test
    fun testOnItemSelectedHomeButton() {
        val menuItem = Mockito.mock(MenuItem::class.java)
        Mockito.`when`(menuItem.itemId).thenReturn(android.R.id.home)
        val result = mainActivity!!.onOptionsItemSelected(menuItem)
        Assert.assertTrue(result)
    }

    // new tests
    @Test
    fun testOnOptionsItemSelectedOpenDrawer() {
        val menuItem = Mockito.mock(MenuItem::class.java)
        Mockito.`when`(menuItem.itemId).thenReturn(android.R.id.home)
        mainActivity!!.onOptionsItemSelected(menuItem)
        mainActivity?.drawerLayout?.isDrawerOpen(mainActivity!!.fragmentContainer)
    }

    @Test
    fun testOnOptionsItemSelectedSearch() {
        val searchFragment = mainActivity!!.searchFragment
        // assert the search fragment is NOT already added
        Assert.assertFalse(searchFragment!!.isAdded)
        val menuItem = Mockito.mock(MenuItem::class.java)
        Mockito.`when`(menuItem.itemId).thenReturn(R.id.action_search)
        // select the search option item
        mainActivity!!.onOptionsItemSelected(menuItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        // assert the search fragment IS now added
        Assert.assertTrue(searchFragment.isAdded)
        // post test remove the added fragment
        mainActivity!!.supportFragmentManager.beginTransaction().remove(mainActivity!!.searchFragment!!)
                .commit()
    }

    @Test
    fun testNavigationItemSelected() {
        val menuItem = Mockito.mock(MenuItem::class.java)
//        val drawerLayoutSpy = Mockito.spy<DrawerLayout>(mainActivity.drawerLayout)
//        drawerLayout = drawerLayoutSpy
//        mainActivity!!.onNavigationItemSelected(menuItem)
//        Mockito.verify(menuItem, Mockito.times(1)).isChecked = true
//        Mockito.verify(drawerLayoutSpy, Mockito.times(1)).closeDrawers()
    }

    @Test
    fun testOnChildrenLoadedForRootCategory() {
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
        mainActivity!!.onChildrenLoaded(parentId, children)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val numberOfChildFragments = mainActivity!!.adapter!!.itemCount
        Assert.assertEquals(expectedNumOfFragmentsCreated.toLong(), numberOfChildFragments.toLong())
    }
}