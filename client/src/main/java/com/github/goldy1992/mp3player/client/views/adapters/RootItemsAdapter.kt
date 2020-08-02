package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.ArrayList

class RootItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val menuCategories = sortedMapOf<MediaItemType, MediaBrowserCompat.MediaItem>()
    var pagerItems = sortedMapOf<MediaItemType, MediaItemListFragment>()

    /**     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    /** */
    override fun getItemCount(): Int {
        return pagerItems.size
    }

    /**  */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    private fun getMediaItemTypeFromPosition(position: Int): MediaItemType {
        val categoryArrayList = ArrayList(menuCategories.keys)
        return categoryArrayList[position]
    }
}