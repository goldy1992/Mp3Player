package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.core.view.GravityCompat
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
import com.github.goldy1992.mp3player.client.views.ThemeSpinnerController
import com.github.goldy1992.mp3player.client.views.adapters.MyPagerAdapter
import com.github.goldy1992.mp3player.client.views.fragments.SearchFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.FolderListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

abstract class MainActivity : MediaActivityCompat(), MediaBrowserResponseListener {

    private var tabLayoutMediator: TabLayoutMediator? = null

    var adapter: MyPagerAdapter? = null

    var searchFragment: SearchFragment? = null

    @Inject
    lateinit var myDrawerListener: MyDrawerListener

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    override fun initialiseView(@LayoutRes layoutId: Int): Boolean {
        setContentView(layoutId)
        searchFragment = SearchFragment()
        appBarLayout!!.addOnOffsetChangedListener(OnOffsetChangedListener { app: AppBarLayout?, offset: Int ->
            Log.i(logTag(), "offset: " + offset + ", scroll range: " + app?.totalScrollRange)
            var newOffset = offset
                newOffset += app!!.totalScrollRange
            rootMenuItemsPager?.setPadding(
                    rootMenuItemsPager.paddingLeft,
                    rootMenuItemsPager.paddingTop,
                    rootMenuItemsPager.paddingRight,
                    newOffset)
        })
        adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        rootMenuItemsPager!!.setAdapter(adapter)
        tabLayoutMediator = TabLayoutMediator(tabLayout, rootMenuItemsPager!!, adapter!!)
        tabLayoutMediator!!.attach()
        rootMenuItemsPager!!.setAdapter(adapter)
        setSupportActionBar(titleToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        mediaBrowserAdapter.registerRootListener(this)

        initNavigationView()
        drawerLayout.addDrawerListener(myDrawerListener)
        return true
    }

    public override fun onResume() {
        super.onResume()
        if (null != searchFragment && searchFragment!!.isAdded && searchFragment!!.isVisible) {
            supportFragmentManager
                    .beginTransaction()
                    .remove(searchFragment!!)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.action_search -> {
                Log.i(logTag(), "hit action search")
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, searchFragment!!, "SEARCH_FGMT")
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .commit()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    // MediaBrowserConnectorCallback
    override fun onConnected() {
        super.onConnected()
        mediaBrowserAdapter.subscribeToRoot()
        initialiseView(R.layout.activity_main)
    }

    @VisibleForTesting
    fun onNavigationItemSelected(menuItem: MenuItem): Boolean { // set item as selected to persist highlight
        menuItem.isChecked = true
        // close drawer when item is tapped
        drawerLayout!!.closeDrawers()
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true
    }

    private fun initNavigationView() {
        navigationView!!.setNavigationItemSelectedListener { menuItem: MenuItem -> onNavigationItemSelected(menuItem) }
        val spinner = navigationView!!.menu.findItem(R.id.themes_menu_item).actionView as Spinner
        ThemeSpinnerController(applicationContext, spinner, this, componentClassMapper)
    }

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaBrowserCompat.MediaItem>) {
        val rootItemsOrdered = TreeSet(compareRootMediaItemsByMediaItemType)
        rootItemsOrdered.addAll(children)
        for (mediaItem in rootItemsOrdered) {
            val id = MediaItemUtils.getMediaId(mediaItem)!!
            Log.i(logTag(), "media id: $id")
            val category = MediaItemUtils.getExtra(Constants.ROOT_ITEM_TYPE, mediaItem) as MediaItemType
            var mediaItemListFragment: MediaItemListFragment?
            mediaItemListFragment = when (category) {
                MediaItemType.SONGS -> SongListFragment.newInstance(category, id, mediaActivityCompatComponent)
                MediaItemType.FOLDERS -> FolderListFragment.newInstance(category, id, mediaActivityCompatComponent)
                else -> null
            }
            if (null != mediaItemListFragment) {
                adapter!!.pagerItems[category] = mediaItemListFragment
                adapter!!.menuCategories[category] = mediaItem
                adapter!!.notifyDataSetChanged()

            }
        }
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

}