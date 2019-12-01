package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator.OnConfigureTabCallback
import java.util.*

class MyPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle?) : FragmentStateAdapter(fm!!, lifecycle!!), OnConfigureTabCallback {
    val menuCategories: Map<MediaItemType, MediaBrowserCompat.MediaItem> = TreeMap()
    val pagerItems: Map<MediaItemType, MediaItemListFragment> = TreeMap()
    private fun getMediaItemTypeFromPosition(position: Int): MediaItemType {
        val categoryArrayList = ArrayList(menuCategories.keys)
        return categoryArrayList[position]
    }

    override fun createFragment(position: Int): Fragment {
        val category = getMediaItemTypeFromPosition(position)
        return pagerItems[category]!!
    }

    override fun getItemCount(): Int {
        return pagerItems.size
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        val categoryArrayList = ArrayList(menuCategories.keys)
        val category = categoryArrayList[position]
        val i = menuCategories[category]
        tab.text = i!!.description.title
    }
}